package com.example.bbtotal

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_NAME,
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "setMockLocation" -> {
                    mockLocation = call.arguments as? Map<String, Any?>
                    result.success(null)
                }

                "getMockLocation" -> result.success(mockLocation)

                "clearMockLocation" -> {
                    mockLocation = null
                    result.success(null)
                }

                else -> result.notImplemented()
            }
        }
    }

    companion object {
        private const val CHANNEL_NAME = "bbtotal/mock_location"
        private var mockLocation: Map<String, Any?>? = null
    }
}
