import Flutter
import UIKit
import WebKit

final class NativeCheckInWebViewFactory: NSObject, FlutterPlatformViewFactory {
  static let viewType = "bbtotal/native_check_in_webview"

  private let messenger: FlutterBinaryMessenger

  init(messenger: FlutterBinaryMessenger) {
    self.messenger = messenger
    super.init()
  }

  func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
    return FlutterStandardMessageCodec.sharedInstance()
  }

  func create(
    withFrame frame: CGRect,
    viewIdentifier viewId: Int64,
    arguments args: Any?
  ) -> FlutterPlatformView {
    let creationParams = args as? [String: Any] ?? [:]
    return NativeCheckInWebView(
      frame: frame,
      messenger: messenger,
      viewId: viewId,
      creationParams: creationParams
    )
  }
}

final class NativeCheckInWebView: NSObject, FlutterPlatformView {
  private let methodChannel: FlutterMethodChannel
  private let startupScript: String
  private let urlString: String
  private let webView: WKWebView
  private let nativeBridgeHandler: WeakScriptMessageHandler
  private let syAppModelHandler: WeakScriptMessageHandler

  init(
    frame: CGRect,
    messenger: FlutterBinaryMessenger,
    viewId: Int64,
    creationParams: [String: Any]
  ) {
    self.startupScript = creationParams["startupScript"] as? String ?? ""
    self.urlString = creationParams["url"] as? String ?? ""

    let userContentController = WKUserContentController()
    userContentController.addUserScript(
      WKUserScript(
        source: startupScript,
        injectionTime: .atDocumentStart,
        forMainFrameOnly: false
      )
    )

    let configuration = WKWebViewConfiguration()
    configuration.userContentController = userContentController
    configuration.preferences.javaScriptEnabled = true

    self.webView = WKWebView(frame: frame, configuration: configuration)
    self.methodChannel = FlutterMethodChannel(
      name: "bbtotal/native_check_in_webview/\(viewId)",
      binaryMessenger: messenger
    )
    self.nativeBridgeHandler = WeakScriptMessageHandler()
    self.syAppModelHandler = WeakScriptMessageHandler()

    super.init()

    nativeBridgeHandler.delegate = self
    syAppModelHandler.delegate = self

    userContentController.add(
      nativeBridgeHandler,
      name: Self.nativeBridgeHandlerName
    )
    userContentController.add(
      syAppModelHandler,
      name: Self.syAppModelHandlerName
    )

    webView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
    webView.navigationDelegate = self
    methodChannel.setMethodCallHandler { [weak self] call, result in
      self?.handle(call: call, result: result)
    }

    if let url = URL(string: urlString) {
      webView.load(URLRequest(url: url))
    } else {
      sendEvent([
        "type": "error",
        "description": "Missing WebView URL."
      ])
    }
  }

  deinit {
    methodChannel.setMethodCallHandler(nil)
    webView.configuration.userContentController.removeScriptMessageHandler(
      forName: Self.nativeBridgeHandlerName
    )
    webView.configuration.userContentController.removeScriptMessageHandler(
      forName: Self.syAppModelHandlerName
    )
  }

  func view() -> UIView {
    return webView
  }

  private func handle(call: FlutterMethodCall, result: FlutterResult) {
    switch call.method {
    case "applyPreset":
      applyPreset()
      result(nil)
    case "reload":
      webView.reload()
      result(nil)
    default:
      result(FlutterMethodNotImplemented)
    }
  }

  private func applyPreset() {
    evaluateJavaScript(
      "window.__bbtotalApplyPreset && window.__bbtotalApplyPreset();"
    )
  }

  private func notifyUserinfo() {
    evaluateJavaScript(
      "window.__bbtotalNotifyUserinfo && window.__bbtotalNotifyUserinfo();"
    )
  }

  private func notifyWifiinfo() {
    evaluateJavaScript(
      "window.__bbtotalNotifyWifiinfo && window.__bbtotalNotifyWifiinfo();"
    )
  }

  private func evaluateJavaScript(_ script: String) {
    guard !script.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
      return
    }

    DispatchQueue.main.async { [weak self] in
      self?.webView.evaluateJavaScript(script, completionHandler: nil)
    }
  }

  private func handleBridgeMessage(_ rawMessage: String?) {
    guard let command = decodeCommand(rawMessage) else {
      if let rawMessage, !rawMessage.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
        sendLog("Ignored bridge message: \(rawMessage)")
      }
      return
    }

    let type = nonEmptyString(command["type"]) ?? nonEmptyString(command["function"]) ?? ""

    switch type {
    case "getLocation", "getUpdatingLocation":
      applyPreset()
    case "getUserinfo", "getUserInfo":
      notifyUserinfo()
    case "getWifiinfo":
      notifyWifiinfo()
    case "hiddenTitle":
      sendBridgeControl(type: "hiddenTitle")
    case "config":
      let hideNavValue = command["hideNav"]
      let shouldHide = boolValue(hideNavValue)
      sendBridgeControl(type: shouldHide ? "hiddenTitle" : "showTitle")
    case "reloadData":
      sendBridgeControl(type: "reload")
      webView.reload()
    case "yuyueMeeting", "endMeeting", "joinyuyueMeeting", "joinyuyueZhibo", "launchMiniProgram":
      sendLog("Native action requested: \(command)")
    default:
      if let url = nonEmptyString(command["openUrl"]) {
        sendEvent([
          "type": "bridgeControl",
          "command": [
            "type": "openUrl",
            "url": url
          ]
        ])
      } else if boolValue(command["pop"]) {
        sendBridgeControl(type: "pop")
      } else {
        sendLog("Bridge passthrough: \(command)")
      }
    }
  }

  private func decodeCommand(_ rawMessage: String?) -> [String: Any]? {
    guard
      let rawMessage,
      let data = rawMessage.data(using: .utf8),
      let object = try? JSONSerialization.jsonObject(with: data),
      let command = object as? [String: Any]
    else {
      return nil
    }

    return command
  }

  private func normalizeMessageString(body: Any) -> String? {
    if let bodyString = body as? String {
      return bodyString
    }

    guard JSONSerialization.isValidJSONObject(body) else {
      return nil
    }

    guard
      let data = try? JSONSerialization.data(withJSONObject: body),
      let bodyString = String(data: data, encoding: .utf8)
    else {
      return nil
    }

    return bodyString
  }

  private func nonEmptyString(_ value: Any?) -> String? {
    guard let string = value as? String else {
      return nil
    }
    return string.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? nil : string
  }

  private func boolValue(_ value: Any?) -> Bool {
    switch value {
    case let boolValue as Bool:
      return boolValue
    case let stringValue as String:
      return stringValue.caseInsensitiveCompare("YES") == .orderedSame ||
        stringValue.caseInsensitiveCompare("true") == .orderedSame
    default:
      return false
    }
  }

  private func sendBridgeControl(type: String) {
    sendEvent([
      "type": "bridgeControl",
      "command": [
        "type": type
      ]
    ])
  }

  private func sendLog(_ message: String) {
    sendEvent([
      "type": "log",
      "message": message
    ])
  }

  private func sendEvent(_ event: [String: Any]) {
    DispatchQueue.main.async { [weak self] in
      self?.methodChannel.invokeMethod("onEvent", arguments: event)
    }
  }

  private static let nativeBridgeHandlerName = "bbtotalNativeBridge"
  private static let syAppModelHandlerName = "SYAppModel"
}

private final class WeakScriptMessageHandler: NSObject, WKScriptMessageHandler {
  weak var delegate: WKScriptMessageHandler?

  func userContentController(
    _ userContentController: WKUserContentController,
    didReceive message: WKScriptMessage
  ) {
    delegate?.userContentController(
      userContentController,
      didReceive: message
    )
  }
}

extension NativeCheckInWebView: WKNavigationDelegate {
  func webView(
    _ webView: WKWebView,
    didStartProvisionalNavigation navigation: WKNavigation!
  ) {
    sendEvent([
      "type": "pageStarted",
      "url": webView.url?.absoluteString ?? urlString
    ])
  }

  func webView(
    _ webView: WKWebView,
    didFinish navigation: WKNavigation!
  ) {
    sendEvent([
      "type": "pageFinished",
      "url": webView.url?.absoluteString ?? urlString
    ])
  }

  func webView(
    _ webView: WKWebView,
    didFail navigation: WKNavigation!,
    withError error: Error
  ) {
    sendEvent([
      "type": "error",
      "description": error.localizedDescription
    ])
  }

  func webView(
    _ webView: WKWebView,
    didFailProvisionalNavigation navigation: WKNavigation!,
    withError error: Error
  ) {
    sendEvent([
      "type": "error",
      "description": error.localizedDescription
    ])
  }
}

extension NativeCheckInWebView: WKScriptMessageHandler {
  func userContentController(
    _ userContentController: WKUserContentController,
    didReceive message: WKScriptMessage
  ) {
    handleBridgeMessage(normalizeMessageString(body: message.body))
  }
}
