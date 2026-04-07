# BBTotal Hybrid MVP

This repository is now a **minimal Flutter APK shell** for embedding a frequently changing business check-in page.

The project intentionally does **not** try to rebuild the page logic natively and does **not** use `webview_flutter + runJavaScript` anymore.

## Why This Direction

The target page changes often and carries many noisy request parameters. Rebuilding the request chain by hand is fragile.

The new MVP follows one rule:

- keep the business page embedded
- keep the APK as the native container
- let the container provide session/bootstrap/bridge/diagnostics
- avoid patching page logic after load

`flutter_inappwebview` was chosen because its official docs support:

- `initialUserScripts` with `AT_DOCUMENT_START`
- JavaScript handlers for Flutter communication
- cookie management before loading a page
- better WebView control than the lightweight default Flutter wrapper

## MVP Scope

The current MVP focuses on:

1. Query mobile token and user info by username
2. Prepare bridge location from current coordinates or recent history
3. Query `cheque / ticket`
4. Open the target page inside `InAppWebView` with:
   - document-start session/bootstrap script
   - sync bridge methods for early page access
   - diagnostics for console/navigation/bridge events

## Current Architecture

### UI

- `lib/pages/hybrid_checkin_page.dart`
  - one-page hybrid shell
  - business URL input
  - token/cheque/history/location preparation
  - embedded `InAppWebView`
  - diagnostics panel

### Bridge and Runtime

- `lib/models/hybrid_runtime_context.dart`
  - builds the resolved URL
  - prepares cookies, storage seeds, auth headers
  - prepares the sync bridge runtime payload

- `lib/services/hybrid_bridge_service.dart`
  - bootstraps cookies before loading the page
  - injects document-start user scripts
  - registers Flutter JavaScript handlers

- `lib/services/hybrid_diagnostics_service.dart`
  - stores container logs in a small ring buffer

### Business Data Helpers

- `lib/services/user_login_info_service.dart`
- `lib/services/check_in_history_service.dart`
- `lib/services/cheque_service.dart`
- `lib/services/location_service.dart`

## Why The New Bridge Is Different

The previous attempt relied on `runJavaScript` after navigation events. That leaves a race window.

The new MVP injects a script at document start and exposes **synchronous** bridge data:

- `getToken`
- `getUserinfo`
- `getUserInfo`
- `getLocation`
- `getUpdatingLocation`
- `getWifiinfo`

Asynchronous handlers are only used for lower-risk actions:

- diagnostics logging
- `postMessage`
- `openUrl`
- `closePage`

This avoids depending on late bridge availability for the core session data.

## Important Limitation

On Android, the `flutter_inappwebview` docs note that JavaScript handler availability can depend on the platform-ready event if the underlying WebView does not support document-start behavior fully.

Because of that, this MVP keeps the critical bridge methods synchronous and embeds the runtime data directly into the injected script.

## Suggested Next Steps

1. Verify whether the target page reads session data from:
   - query params
   - cookies
   - localStorage/sessionStorage
   - sync bridge methods
2. Add only the minimum extra bridge methods that the page really calls
3. If the page still depends on more browser/container behavior, add it inside `HybridBridgeService`
4. Only if a device-specific limitation appears, add a tiny Android plugin instead of rebuilding the whole shell

## Repository Goal

This repository should stay small:

- one Flutter APK shell
- one hybrid page host
- one bridge service
- minimal business bootstrap services

No iOS target, no mock location, no custom native WebView implementation unless real device behavior forces it.
