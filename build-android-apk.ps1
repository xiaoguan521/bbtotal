[CmdletBinding()]
param(
    [ValidateSet('debug', 'release')]
    [string]$BuildMode = 'release',

    [ValidateSet('android-arm64', 'android-arm', 'android-x64')]
    [string]$TargetPlatform = 'android-arm64',
    [bool]$SplitPerAbi = $true,
    [bool]$Shrink = $false,

    [string]$JavaHome = 'C:\Users\xiaochen\.sdkman\candidates\java\17.0.9-tem',
    [string]$FlutterHome = 'E:\flutter',
    [string]$PubCache = 'E:\pub-cache',
    [string]$AndroidHome = 'E:\Android\sdk',
    [string]$AndroidSdkRoot = 'E:\Android\sdk',
    [string]$GradleUserHome = '',
    [string]$SharedGradleCacheHome = 'D:\program\Java\repository\gradle-flutter-mcp-studio',
    [string]$LogPath = '',

    [switch]$SkipClean,
    [switch]$SkipPubGet
)

$ErrorActionPreference = 'Stop'

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir

if ([string]::IsNullOrWhiteSpace($GradleUserHome)) {
    $GradleUserHome = Join-Path $projectDir '.gradle-user-home'
}

$logDir = Join-Path $projectDir 'build_logs'
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

if ([string]::IsNullOrWhiteSpace($LogPath)) {
    $timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
    $LogPath = Join-Path $logDir "build-android-apk-$timestamp.log"
}

New-Item -ItemType File -Force -Path $LogPath | Out-Null

function Write-Log {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Message
    )

    $line = "[{0}] {1}" -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), $Message
    Write-Host $line
    Add-Content -LiteralPath $LogPath -Value $line
}

function Invoke-LoggedCommand {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Label,

        [Parameter(Mandatory = $true)]
        [scriptblock]$Command
    )

    Write-Log "BEGIN $Label"

    try {
        & $Command 2>&1 | Tee-Object -FilePath $LogPath -Append
        $exitCode = $LASTEXITCODE
        if ($exitCode -ne 0) {
            throw "$Label failed with exit code $exitCode"
        }
    } catch {
        Write-Log "FAIL $Label :: $($_.Exception.Message)"
        throw
    }

    Write-Log "END $Label"
}

function Ensure-Directory {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    New-Item -ItemType Directory -Force -Path $Path | Out-Null
}

function Ensure-Junction {
    param(
        [Parameter(Mandatory = $true)]
        [string]$LinkPath,

        [Parameter(Mandatory = $true)]
        [string]$TargetPath
    )

    if (-not (Test-Path -LiteralPath $TargetPath -PathType Container)) {
        return
    }

    if (Test-Path -LiteralPath $LinkPath) {
        return
    }

    Ensure-Directory -Path (Split-Path -Parent $LinkPath)
    New-Item -ItemType Junction -Path $LinkPath -Target $TargetPath | Out-Null
}

function Prepare-GradleUserHome {
    param(
        [Parameter(Mandatory = $true)]
        [string]$LocalGradleHome,

        [Parameter(Mandatory = $true)]
        [string]$MirrorScriptPath,

        [string]$SharedGradleHome
    )

    Ensure-Directory -Path $LocalGradleHome
    Ensure-Directory -Path (Join-Path $LocalGradleHome 'caches')
    Ensure-Directory -Path (Join-Path $LocalGradleHome 'wrapper')
    Ensure-Directory -Path (Join-Path $LocalGradleHome 'init.d')

    Copy-Item -LiteralPath $MirrorScriptPath -Destination (Join-Path $LocalGradleHome 'init.d\repository-mirrors.init.gradle') -Force

    if (-not [string]::IsNullOrWhiteSpace($SharedGradleHome) -and (Test-Path -LiteralPath $SharedGradleHome -PathType Container)) {
        Ensure-Junction -LinkPath (Join-Path $LocalGradleHome 'wrapper\dists') -TargetPath (Join-Path $SharedGradleHome 'wrapper\dists')
        Ensure-Junction -LinkPath (Join-Path $LocalGradleHome 'caches\modules-2') -TargetPath (Join-Path $SharedGradleHome 'caches\modules-2')
    }
}

$env:JAVA_HOME = $JavaHome
$env:FLUTTER_HOME = $FlutterHome
$env:PUB_CACHE = $PubCache
$env:ANDROID_HOME = $AndroidHome
$env:ANDROID_SDK_ROOT = $AndroidSdkRoot
$env:GRADLE_USER_HOME = $GradleUserHome

Prepare-GradleUserHome `
    -LocalGradleHome $env:GRADLE_USER_HOME `
    -SharedGradleHome $SharedGradleCacheHome `
    -MirrorScriptPath (Join-Path $projectDir 'android\repository-mirrors.init.gradle')

function Assert-Directory {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path,

        [Parameter(Mandatory = $true)]
        [string]$Label
    )

    if (-not (Test-Path -LiteralPath $Path -PathType Container)) {
        throw "$Label directory not found: $Path"
    }
}

Assert-Directory -Path $env:JAVA_HOME -Label 'JAVA_HOME'
Assert-Directory -Path $env:FLUTTER_HOME -Label 'FLUTTER_HOME'
Assert-Directory -Path $env:ANDROID_HOME -Label 'ANDROID_HOME'
Assert-Directory -Path $env:ANDROID_SDK_ROOT -Label 'ANDROID_SDK_ROOT'
Assert-Directory -Path $env:GRADLE_USER_HOME -Label 'GRADLE_USER_HOME'

if (-not (Test-Path -LiteralPath $env:PUB_CACHE -PathType Container)) {
    New-Item -ItemType Directory -Force -Path $env:PUB_CACHE | Out-Null
}

$extraPaths = @(
    (Join-Path $env:JAVA_HOME 'bin'),
    (Join-Path $env:FLUTTER_HOME 'bin'),
    (Join-Path $env:ANDROID_SDK_ROOT 'platform-tools'),
    (Join-Path $env:ANDROID_SDK_ROOT 'cmdline-tools\latest\bin')
)

$env:Path = (($extraPaths + ($env:Path -split ';')) | Select-Object -Unique | Where-Object { $_ }) -join ';'

$processNames = @('cmd.exe', 'java.exe', 'dart.exe', 'dartaotruntime.exe', 'dartvm.exe')
$commandLinePatterns = @(
    '*bbtotal*',
    '*gradle-wrapper.jar*',
    '*flutter_tools.snapshot*',
    '*frontend_server_aot*',
    '*flutter build apk*'
)

$staleProcesses = @()

try {
    $staleProcesses = Get-CimInstance Win32_Process | Where-Object {
        $process = $_
        if ($process.Name -notin $processNames) {
            return $false
        }

        foreach ($pattern in $commandLinePatterns) {
            if ($process.CommandLine -like $pattern) {
                return $true
            }
        }

        return $false
    }
} catch {
    Write-Warning "Skipping stale process cleanup: $($_.Exception.Message)"
}

if ($staleProcesses) {
    $staleProcesses | ForEach-Object {
        Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue
    }
}

Write-Host "ProjectDir      : $projectDir"
Write-Host "BuildMode       : $BuildMode"
Write-Host "TargetPlatform  : $TargetPlatform"
Write-Host "SplitPerAbi     : $SplitPerAbi"
Write-Host "Shrink          : $Shrink"
Write-Host "JAVA_HOME       : $env:JAVA_HOME"
Write-Host "FLUTTER_HOME    : $env:FLUTTER_HOME"
Write-Host "PUB_CACHE       : $env:PUB_CACHE"
Write-Host "ANDROID_SDK_ROOT: $env:ANDROID_SDK_ROOT"
Write-Host "GRADLE_USER_HOME: $env:GRADLE_USER_HOME"
Write-Host "GRADLE_SHARED   : $SharedGradleCacheHome"
Write-Host "LogPath         : $LogPath"

Write-Log "ProjectDir      : $projectDir"
Write-Log "BuildMode       : $BuildMode"
Write-Log "TargetPlatform  : $TargetPlatform"
Write-Log "SplitPerAbi     : $SplitPerAbi"
Write-Log "Shrink          : $Shrink"
Write-Log "JAVA_HOME       : $env:JAVA_HOME"
Write-Log "FLUTTER_HOME    : $env:FLUTTER_HOME"
Write-Log "PUB_CACHE       : $env:PUB_CACHE"
Write-Log "ANDROID_SDK_ROOT: $env:ANDROID_SDK_ROOT"
Write-Log "GRADLE_USER_HOME: $env:GRADLE_USER_HOME"
Write-Log "GRADLE_SHARED   : $SharedGradleCacheHome"
Write-Log "LogPath         : $LogPath"

Invoke-LoggedCommand -Label 'java -version' -Command {
    & (Join-Path $env:JAVA_HOME 'bin\java.exe') -version
}

if (-not $SkipClean) {
    Invoke-LoggedCommand -Label 'flutter clean' -Command {
        flutter clean
    }
}

if (-not $SkipPubGet) {
    Invoke-LoggedCommand -Label 'flutter pub get' -Command {
        flutter pub get
    }
}

$buildArgs = @(
    'build',
    'apk',
    "--$BuildMode",
    '--target-platform',
    $TargetPlatform,
    '--suppress-analytics'
)

if ($SplitPerAbi) {
    $buildArgs += '--split-per-abi'
}

if (-not $Shrink) {
    $buildArgs += '--no-shrink'
}

Invoke-LoggedCommand -Label 'flutter build apk' -Command {
    flutter @buildArgs
}

$apkOutputDir = Join-Path $projectDir 'build\app\outputs\flutter-apk'
$apks = @()

if (Test-Path -LiteralPath $apkOutputDir -PathType Container) {
    $apks = Get-ChildItem -Path $apkOutputDir -Filter *.apk -File | Sort-Object Name
}

if (-not $apks) {
    throw "APK not found after build under: $apkOutputDir"
}

Write-Host ''
Write-Host 'APK build completed successfully.'
Write-Log 'APK build completed successfully.'

foreach ($apk in $apks) {
    Write-Host "APK Path        : $($apk.FullName)"
    Write-Host "APK Size (bytes): $($apk.Length)"
    Write-Host "LastWriteTime   : $($apk.LastWriteTime.ToString('yyyy-MM-dd HH:mm:ss'))"

    Write-Log "APK Path        : $($apk.FullName)"
    Write-Log "APK Size (bytes): $($apk.Length)"
    Write-Log "LastWriteTime   : $($apk.LastWriteTime.ToString('yyyy-MM-dd HH:mm:ss'))"
}
