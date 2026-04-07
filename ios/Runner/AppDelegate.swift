import Flutter
import UIKit

@main
@objc class AppDelegate: FlutterAppDelegate {
  private let channelName = "bbtotal/mock_location"
  private var mockLocation: [String: Any]? = nil

  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)

    if let controller = window?.rootViewController as? FlutterViewController {
      let channel = FlutterMethodChannel(
        name: channelName,
        binaryMessenger: controller.binaryMessenger
      )

      channel.setMethodCallHandler { [weak self] call, result in
        guard let self else {
          result(
            FlutterError(
              code: "UNAVAILABLE",
              message: "AppDelegate is unavailable.",
              details: nil
            )
          )
          return
        }

        switch call.method {
        case "setMockLocation":
          self.mockLocation = call.arguments as? [String: Any]
          result(nil)
        case "getMockLocation":
          result(self.mockLocation)
        case "clearMockLocation":
          self.mockLocation = nil
          result(nil)
        default:
          result(FlutterMethodNotImplemented)
        }
      }
    }

    return super.application(
      application,
      didFinishLaunchingWithOptions: launchOptions
    )
  }
}
