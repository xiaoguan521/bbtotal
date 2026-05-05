#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

mode="${1:-debug}"
case "$mode" in
  debug|release) ;;
  *)
    echo "Usage: $0 [debug|release]" >&2
    exit 2
    ;;
esac

image_name="${BBTOTAL_ANDROID_BUILDER_IMAGE:-bbtotal-android-builder}"
cache_root="${BBTOTAL_DOCKER_CACHE_DIR:-$PWD/.docker}"

mkdir -p "$cache_root/pub-cache" "$cache_root/gradle-cache"

docker build \
  -f Dockerfile.android \
  -t "$image_name" \
  .

docker run --rm \
  -e PUB_CACHE=/root/.pub-cache \
  -e GRADLE_USER_HOME=/root/.gradle \
  -e GRADLE_OPTS="-Dorg.gradle.vfs.watch=false" \
  -e ANDROID_KEYSTORE_PATH="${ANDROID_KEYSTORE_PATH:-}" \
  -e ANDROID_KEYSTORE_PASSWORD="${ANDROID_KEYSTORE_PASSWORD:-}" \
  -e ANDROID_KEY_ALIAS="${ANDROID_KEY_ALIAS:-}" \
  -e ANDROID_KEY_PASSWORD="${ANDROID_KEY_PASSWORD:-}" \
  -v "$PWD:/workspace" \
  -v "$cache_root/pub-cache:/root/.pub-cache" \
  -v "$cache_root/gradle-cache:/root/.gradle" \
  -w /workspace \
  "$image_name" \
  bash -lc "flutter pub get && flutter build apk --$mode"

echo "APK output:"
ls -lh "build/app/outputs/flutter-apk/app-$mode.apk"
