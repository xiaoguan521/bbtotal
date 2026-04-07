package com.example.bbtotal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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
    override fun create(
        context: Context,
        viewId: Int,
        args: Any?,
    ): PlatformView {
        val creationParams = args as? Map<*, *> ?: emptyMap<String, Any?>()
        return NativeCheckInWebView(
            context = context,
            messenger = messenger,
            viewId = viewId,
            creationParams = creationParams,
        )
    }

    companion object {
        const val VIEW_TYPE = "bbtotal/native_check_in_webview"
    }
}

@SuppressLint("SetJavaScriptEnabled")
private class NativeCheckInWebView(
    context: Context,
    messenger: BinaryMessenger,
    viewId: Int,
    creationParams: Map<*, *>,
) : PlatformView {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val methodChannel = MethodChannel(
        messenger,
        "bbtotal/native_check_in_webview/$viewId",
    )
    private val webView = WebView(context)

    private val url = creationParams.stringValue("url")
    private val allowedOriginRule = creationParams.stringValue("allowedOriginRule")
    private val locationPayloadJson =
        creationParams.stringValue("locationPayloadJson", "{}")
    private val userinfoResultJson =
        creationParams.stringValue("userinfoResultJson", "{}")
    private val wifiInfoJson =
        creationParams.stringValue("wifiInfoJson", "{}")
    private val startupScript =
        creationParams.stringValue("startupScript")

    private val supportsDocumentStartScript =
        WebViewFeature.isFeatureSupported(WebViewFeature.DOCUMENT_START_SCRIPT)

    init {
        methodChannel.setMethodCallHandler(::onMethodCall)

        webView.layoutParams = android.widget.FrameLayout.LayoutParams(
            MATCH_PARENT,
            MATCH_PARENT,
        )
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(
            NativeBridgeBackend(),
            NATIVE_BRIDGE_NAME,
        )
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: android.graphics.Bitmap?,
            ) {
                sendEvent(
                    mapOf(
                        "type" to "pageStarted",
                        "url" to (url ?: ""),
                    ),
                )
                maybeInjectFallbackScript()
            }

            override fun onPageFinished(
                view: WebView?,
                url: String?,
            ) {
                maybeInjectFallbackScript()
                sendEvent(
                    mapOf(
                        "type" to "pageFinished",
                        "url" to (url ?: ""),
                    ),
                )
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                if (request?.isForMainFrame == false) {
                    return
                }
                sendEvent(
                    mapOf(
                        "type" to "error",
                        "description" to (
                            error?.description?.toString()
                                ?: "Unknown Android WebView error"
                            ),
                    ),
                )
            }

            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {
                sendEvent(
                    mapOf(
                        "type" to "error",
                        "description" to (description ?: "Unknown Android WebView error"),
                    ),
                )
            }
        }

        if (supportsDocumentStartScript) {
            try {
                WebViewCompat.addDocumentStartJavaScript(
                    webView,
                    startupScript,
                    setOf(allowedOriginRule),
                )
                sendLog("Android document-start injection enabled.")
            } catch (error: Exception) {
                sendLog("Android document-start injection failed: ${error.message}")
            }
        } else {
            sendLog(
                "Android WebView lacks DOCUMENT_START_SCRIPT. Falling back to early evaluateJavascript injection.",
            )
        }

        if (url.isNotBlank()) {
            webView.loadUrl(url)
        } else {
            sendEvent(
                mapOf(
                    "type" to "error",
                    "description" to "Missing WebView URL.",
                ),
            )
        }
    }

    override fun getView(): View = webView

    override fun dispose() {
        methodChannel.setMethodCallHandler(null)
        webView.stopLoading()
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.removeJavascriptInterface(NATIVE_BRIDGE_NAME)
        webView.destroy()
    }

    private fun onMethodCall(
        call: MethodCall,
        result: MethodChannel.Result,
    ) {
        when (call.method) {
            "applyPreset" -> {
                applyPreset()
                result.success(null)
            }

            "reload" -> {
                mainHandler.post {
                    webView.reload()
                }
                result.success(null)
            }

            else -> result.notImplemented()
        }
    }

    private fun maybeInjectFallbackScript() {
        if (supportsDocumentStartScript || startupScript.isBlank()) {
            return
        }
        evaluateScript(startupScript)
    }

    private fun applyPreset() {
        evaluateScript(
            "window.__bbtotalApplyPreset && window.__bbtotalApplyPreset();",
        )
    }

    private fun notifyUserinfo() {
        evaluateScript(
            "window.__bbtotalNotifyUserinfo && window.__bbtotalNotifyUserinfo();",
        )
    }

    private fun notifyWifiinfo() {
        evaluateScript(
            "window.__bbtotalNotifyWifiinfo && window.__bbtotalNotifyWifiinfo();",
        )
    }

    private fun evaluateScript(script: String) {
        if (script.isBlank()) {
            return
        }
        mainHandler.post {
            webView.evaluateJavascript(script, null)
        }
    }

    private fun handleBridgeMessage(rawMessage: String?) {
        val command = decodeCommand(rawMessage) ?: run {
            if (!rawMessage.isNullOrBlank()) {
                sendLog("Ignored bridge message: $rawMessage")
            }
            return
        }

        val type = command.optString("type").ifBlank {
            command.optString("function")
        }

        when {
            type == "getLocation" || type == "getUpdatingLocation" -> applyPreset()
            type == "getUserinfo" || type == "getUserInfo" -> notifyUserinfo()
            type == "getWifiinfo" -> notifyWifiinfo()
            type == "hiddenTitle" -> sendBridgeControl("hiddenTitle")
            type == "config" -> {
                val hideNav = command.opt("hideNav")
                val shouldHide = when (hideNav) {
                    is Boolean -> hideNav
                    is String -> {
                        hideNav.equals("YES", ignoreCase = true) ||
                            hideNav.equals("true", ignoreCase = true)
                    }

                    else -> false
                }
                sendBridgeControl(if (shouldHide) "hiddenTitle" else "showTitle")
            }

            command.optBoolean("pop") -> sendBridgeControl("pop")
            command.has("openUrl") -> {
                sendEvent(
                    mapOf(
                        "type" to "bridgeControl",
                        "command" to mapOf(
                            "type" to "openUrl",
                            "url" to command.optString("openUrl"),
                        ),
                    ),
                )
            }

            type == "reloadData" -> {
                sendBridgeControl("reload")
                mainHandler.post {
                    webView.reload()
                }
            }

            type == "yuyueMeeting" ||
                type == "endMeeting" ||
                type == "joinyuyueMeeting" ||
                type == "joinyuyueZhibo" ||
                type == "launchMiniProgram" -> {
                sendLog("Native action requested: $command")
            }

            else -> sendLog("Bridge passthrough: $command")
        }
    }

    private fun decodeCommand(rawMessage: String?): JSONObject? {
        val trimmed = rawMessage?.trim().orEmpty()
        if (trimmed.isEmpty()) {
            return null
        }

        return try {
            JSONObject(trimmed)
        } catch (_: Exception) {
            null
        }
    }

    private fun sendBridgeControl(type: String) {
        sendEvent(
            mapOf(
                "type" to "bridgeControl",
                "command" to mapOf("type" to type),
            ),
        )
    }

    private fun sendLog(message: String) {
        sendEvent(
            mapOf(
                "type" to "log",
                "message" to message,
            ),
        )
    }

    private fun sendEvent(event: Map<String, Any?>) {
        mainHandler.post {
            methodChannel.invokeMethod("onEvent", event)
        }
    }

    private inner class NativeBridgeBackend {
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
            handleBridgeMessage(message)
        }
    }

    companion object {
        private const val NATIVE_BRIDGE_NAME = "bbtotalNativeBridge"
    }
}

private fun Map<*, *>.stringValue(
    key: String,
    defaultValue: String = "",
): String {
    return this[key]?.toString() ?: defaultValue
}
