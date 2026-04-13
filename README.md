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

## GitHub Remote Bridge

The app now supports a three-level bridge bootstrap fallback:

1. remote bundle from GitHub
2. last successfully cached bundle on device
3. embedded bundle shipped inside the APK

That means bridge-rule updates can be shipped without reinstalling the APK, as long as the change stays inside the page/bootstrap layer.

### Files

- `assets/bootstrap/remote_bridge_config.json`
  - stores the manifest URL used by the app at startup
- `assets/bootstrap/remote_bridge_manifest.json`
  - embedded fallback manifest shipped in the APK
- `assets/bootstrap/remote_bridge_bundle.js`
  - embedded fallback script shipped in the APK
- `remote-bridge/manifest.json`
  - GitHub-hosted manifest template
- `remote-bridge/remote_bridge_bundle.js`
  - GitHub-hosted bridge patch template

### Recommended GitHub Pages URL

If GitHub Pages is enabled for this repository, point the app config to:

`https://<owner>.github.io/<repo>/manifest.json`

Then update:

- [remote_bridge_config.json](/Volumes/移动磁盘/bb/bbtotal/assets/bootstrap/remote_bridge_config.json)

Example:

```json
{
  "manifestUrl": "https://example.github.io/bbtotal/manifest.json"
}
```

### Publish Flow

1. Edit `remote-bridge/manifest.json` and `remote-bridge/remote_bridge_bundle.js`
2. Push to `main`
3. GitHub Actions workflow `Remote Bridge Pages` publishes the directory to GitHub Pages
4. The app loads the new manifest on next startup

### Manifest Format

```json
{
  "version": "2026.04.13.1",
  "scriptUrl": "remote_bridge_bundle.js"
}
```

You can also inline the script:

```json
{
  "version": "2026.04.13.1",
  "script": "window.__bbtotalRemoteBridgeBundle = { version: '2026.04.13.1' };"
}
```

### When APK Reinstall Is Still Needed

Remote bridge updates do not replace native app updates. A new APK is still required when changing:

- Flutter UI code
- Android permissions
- native plugins
- WebView platform behavior
- startup bootstrap code before remote loading begins
