import java.io.File
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("key.properties")
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { input ->
        keystoreProperties.load(input)
    }
}

fun readSigningValue(
    envKey: String,
    propertyKey: String,
): String? {
    val envValue = System.getenv(envKey)?.takeIf { it.isNotBlank() }
    val propertyValue = keystoreProperties
        .getProperty(propertyKey)
        ?.takeIf { it.isNotBlank() }
    return envValue ?: propertyValue
}

fun resolveSigningFile(path: String): File {
    val candidate = File(path)
    return if (candidate.isAbsolute) candidate else rootProject.file(path)
}

val releaseStoreFilePath = readSigningValue(
    envKey = "ANDROID_KEYSTORE_PATH",
    propertyKey = "storeFile",
)
val releaseStorePassword = readSigningValue(
    envKey = "ANDROID_KEYSTORE_PASSWORD",
    propertyKey = "storePassword",
)
val releaseKeyAlias = readSigningValue(
    envKey = "ANDROID_KEY_ALIAS",
    propertyKey = "keyAlias",
)
val releaseKeyPassword = readSigningValue(
    envKey = "ANDROID_KEY_PASSWORD",
    propertyKey = "keyPassword",
)
val hasReleaseSigning = !releaseStoreFilePath.isNullOrBlank() &&
    !releaseStorePassword.isNullOrBlank() &&
    !releaseKeyAlias.isNullOrBlank() &&
    !releaseKeyPassword.isNullOrBlank()

android {
    namespace = "com.example.bbtotal"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        applicationId = "com.example.bbtotal"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("release") {
            if (hasReleaseSigning) {
                storeFile = resolveSigningFile(requireNotNull(releaseStoreFilePath))
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        release {
            signingConfig = if (hasReleaseSigning) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
}

flutter {
    source = "../.."
}
