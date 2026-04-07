package com.example.bbtotal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.ScriptHandler
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import org.json.JSONObject

class NativeCheckInWebViewFactory(
    private val messenger: BinaryMessenger,
) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        @Suppress("UNCHECKED_CAST")
        val creationParams = (args as? Map<*, *>)
            ?.entries
            ?.associate { (key, value) -> key.toString() to value }
            ?: emptyMap()
        return NativeCheckInWebView(
            context = context,
            messenger = messenger,
            viewId = viewId,
            creationParams = creationParams,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private class NativeCheckInWebView(
    context: Context,
    messenger: BinaryMessenger,
    viewId: Int,
    creationParams: Map<String, Any?>,
) : PlatformView {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val channel = MethodChannel(
        messenger,
        "${CHANNEL_PREFIX}_$viewId",
    )
    private val webView = WebView(context)
    private val nativeBridge = NativeBridge()

    private val url = creationParams.stringValue("url")
    private val allowedOriginRule = creationParams.stringValue("allowedOriginRule")
    private val startupScript = creationParams.stringValue("startupScript")
    private val locationPayloadJson = creationParams.stringValue("locationPayloadJson")
    private val userinfoResultJson = creationParams.stringValue("userinfoResultJson")
    private val wifiInfoJson = creationParams.stringValue("wifiInfoJson")

    private var initialized = false
    private var documentStartScriptInstalled = false
    private var documentStartScriptHandler: ScriptHandler? = null
    private var fallbackScriptLogged = false

    init {
        channel.setMethodCallHandler(::handleMethodCall)
    }

    override fun getView(): WebView = webView

    override fun dispose() {
        channel.setMethodCallHandler(null)
        documentStartScriptHandler?.remove()
        documentStartScriptHandler = null
        JS_INTERFACE_NAMES.forEach(webView::removeJavascriptInterface)
        webView.stopLoading()
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.destroy()
    }

    private fun handleMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initialize" -> {
                initializeIfNeeded()
                result.success(null)
            }

            "reload" -> {
                webView.reload()
                result.success(null)
            }

            "applyPreset" -> {
                applyPreset()
                result.success(null)
            }

            else -> result.notImplemented()
        }
    }

    private fun initializeIfNeeded() {
        if (initialized) {
            return
        }
        initialized = true

        WebView.setWebContentsDebuggingEnabled(true)
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            loadWithOverviewMode = true
            useWideViewPort = false
            displayZoomControls = false
            builtInZoomControls = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }

        JS_INTERFACE_NAMES.forEach { interfaceName ->
            webView.addJavascriptInterface(nativeBridge, interfaceName)
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                sendToDart("progress", newProgress)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                sendToDart("pageStarted", url.orEmpty())
                if (!documentStartScriptInstalled && startupScript.isNotBlank()) {
                    if (!fallbackScriptLogged) {
                        fallbackScriptLogged = true
                        sendLog(
                            "DOCUMENT_START_SCRIPT unsupported; falling back to onPageStarted evaluateJavascript.",
                        )
                    }
                    view?.evaluateJavascript(startupScript, null)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                applyPreset()
                sendToDart("pageFinished", url.orEmpty())
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                super.onReceivedError(view, request, error)
                if (request != null && !request.isForMainFrame) {
                    return
                }
                sendToDart(
                    "error",
                    mapOf(
                        "description" to (error?.description?.toString() ?: "unknown error"),
                        "errorCode" to (error?.errorCode ?: 0),
                    ),
                )
            }
        }

        if (startupScript.isNotBlank()) {
            installDocumentStartScriptIfSupported()
        }

        sendLog("Android native WebView debugging enabled. Use chrome://inspect.")
        webView.loadUrl(url)
    }

    private fun installDocumentStartScriptIfSupported() {
        if (!WebViewFeature.isFeatureSupported(WebViewFeature.DOCUMENT_START_SCRIPT)) {
            sendLog("WebViewFeature.DOCUMENT_START_SCRIPT is not supported by this WebView.")
            return
        }

        val originRules = setOf(
            allowedOriginRule.takeIf { it.isNotBlank() } ?: "*",
        )

        try {
            documentStartScriptHandler = WebViewCompat.addDocumentStartJavaScript(
                webView,
                startupScript,
                originRules,
            )
            documentStartScriptInstalled = true
            sendLog(
                "Document-start startup script installed for origin rule ${originRules.first()}.",
            )
        } catch (error: Throwable) {
            sendLog("Document-start startup script install failed: $error")
        }
    }

    private fun applyPreset() {
        webView.evaluateJavascript(
            "window.__bbtotalApplyPreset && window.__bbtotalApplyPreset();",
            null,
        )
    }

    private fun sendLog(message: String) {
        sendToDart("log", message)
    }

    private fun sendBridgeControl(rawCommand: String) {
        sendToDart("bridgeControl", rawCommand)
    }

    private fun sendToDart(method: String, arguments: Any?) {
        mainHandler.post {
            channel.invokeMethod(method, arguments)
        }
    }

    private inner class NativeBridge {
        @JavascriptInterface
        fun getLocation(): String = locationPayloadJson

        @JavascriptInterface
        fun getUpdatingLocation(): String = locationPayloadJson

        @JavascriptInterface
        fun getUserinfo(): String = userinfoResultJson

        @JavascriptInterface
        fun getUserInfo(): String = userinfoResultJson

        @JavascriptInterface
        fun getWifiinfo(): String = wifiInfoJson

        @JavascriptInterface
        fun postMessage(message: String?) {
            if (!message.isNullOrBlank()) {
                sendBridgeControl(message)
            }
        }

        @JavascriptInterface
        fun log(message: String?) {
            if (!message.isNullOrBlank()) {
                sendLog(message)
            }
        }

        @JavascriptInterface
        fun hiddenTitle() {
            sendBridgeControl(jsonOf("type" to "hiddenTitle"))
        }

        @JavascriptInterface
        fun showTitle() {
            sendBridgeControl(jsonOf("type" to "showTitle"))
        }

        @JavascriptInterface
        fun hideNav(hidden: Boolean) {
            sendBridgeControl(
                jsonOf(
                    "function" to "config",
                    "hideNav" to if (hidden) "YES" else "NO",
                ),
            )
        }

        @JavascriptInterface
        fun openUrl(url: String?) {
            sendBridgeControl(jsonOf("type" to "openUrl", "url" to (url ?: "")))
        }

        @JavascriptInterface
        fun pop() {
            sendBridgeControl(jsonOf("type" to "pop"))
        }

        @JavascriptInterface
        fun reloadData() {
            sendBridgeControl(jsonOf("function" to "reloadData"))
        }

        @JavascriptInterface
        fun yuyueMeeting(message: String?) {
            sendBridgeControl(
                jsonOf("type" to "yuyueMeeting", "params" to (message ?: "")),
            )
        }

        @JavascriptInterface
        fun endMeeting(message: String?) {
            sendBridgeControl(
                jsonOf("type" to "endMeeting", "params" to (message ?: "")),
            )
        }

        @JavascriptInterface
        fun joinyuyueMeeting(message: String?) {
            sendBridgeControl(
                jsonOf("type" to "joinyuyueMeeting", "params" to (message ?: "")),
            )
        }

        @JavascriptInterface
        fun joinyuyueZhibo(message: String?) {
            sendBridgeControl(
                jsonOf("type" to "joinyuyueZhibo", "params" to (message ?: "")),
            )
        }
    }

    private fun jsonOf(vararg entries: Pair<String, Any?>): String {
        val json = JSONObject()
        entries.forEach { (key, value) ->
            json.put(key, value)
        }
        return json.toString()
    }

    private fun Map<String, Any?>.stringValue(key: String): String {
        return this[key]?.toString() ?: ""
    }

    private companion object {
        const val CHANNEL_PREFIX = "bbtotal/native_checkin_webview"

        val JS_INTERFACE_NAMES = listOf(
            "bbtotalNativeBridge",
        )
    }
}
