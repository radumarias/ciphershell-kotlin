# ciphershell-kotlin

[![ci](https://github.com/radumarias/rencfs-kotlin/actions/workflows/ci.yml/badge.svg)](https://github.com/radumarias/rencfs-kotlin/actions/workflows/ci.yml)
[![codetriage](https://www.codetriage.com/radumarias/rencfs-kotlin/badges/users.svg)](https://www.codetriage.com/radumarias/rencfs-kotlin)

GUI for [rencfs](https://github.com/radumarias/rencfs) in Koltin Multiplatform with Compose.

It uses the [java-bridge](https://github.com/radumarias/rencfs/tree/main/java-bridge) to interact with Rust code.

> [!WARNING]  
> **This is still under development. Please do not use it with sensitive data for now, please wait for a
stable release.  
> It's mostly ideal for experimental and learning projects. It serves as a reference app for a GUI for `rencfs`.**

## Graphical User Interface Application (GUI app)

Video  
[![Watch the video](https://img.youtube.com/vi/RSA9lR7AxWQ/0.jpg)](https://youtu.be/RSA9lR7AxWQ)

### Integrated Development Environment (IDE)

The multiplatform project is developed mainly using the latest version of Android Studio as of the time of writing this document.

From the Android Studio main window menu, select `File> Open` and then point the explorer to the project' root directory.

If you prefer the command line and have Android Studio installed via the JetBrains installer or by the JetBrains Toolbox app, you can directy start and open the project by running `studio /absolute/path/to/project/root/directory` or  `studio ./relative/path/to/project/root/directory` in the terminal.

From Run Configurations, switch the flavor from rencfsAndroid, rencfsDesktop 

### Build Desktop GUI App

```bash
./gradlew rencfsDesktop:build
```

This will also build `java-bridge` for the current OS target.

### Run Desktop GUI App

It will use the current OS target image for `java-bridge`.

```bash
./gradlew :rencfsDesktop:run
# or
./gradlew clean :rencfsDesktop:run
```

### Build Android App

#### Prerequisites

Update `rencfsAndroid/local.properties` by defining the property `sdk.dir=<full-path-to-android-sdk-dir>` where `<full-path-to-android-sdk-dir>` is the local Android SDK installation directory.
Make sure to have platform and build tools corresponding to API level 35. 

TODO: Add more details how to set up and include command line instructions to achieve a working local development environment from command line

```properties
#sample of local.properties file
sdk.dir=<full-path-to-android-sdk-dir>
```
### Run Android App

There are multiple ways to run the Android App.

#### Via gradle

** Pre-requisites **
Ensure an Android device is connected to your PC and you have fulfilled the above build pre-requisites.

Run `./gradlew :rencfsAndroid:installDebug` in the project root directory

#### Via ADB

**Pre-requisites**

1. Build the apk as described in the previous step.
2. Locate the apk file: `find . -name "*.apk" -print`

Run `adb install -t -r <path-to-apk>`

Example:
`adb install -t -r ./rencfsAndroid/build/outputs/apk/debug/rencfsAndroid-debug.apk`

Launch the app manually via adb:
`adb shell am start -n "rs.xor.rencfs.krencfs/rs.xor.rencfs.krencfs.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER`

At this point, you should also be able to find the App Launcher on your home screen

#### Sideloading

**Pre-requisites** 

Build and locate the apk the same way as the above

Obtain the apk file on the device (various means - download link, email attachment, etc.) and access it (typically via a file manager), choose install and then run it. 

# Contribute

Feel free to fork it, change and use it in any way that you want.
If you build something interesting and feel like sharing pull requests are always appreciated.

## How to contribute

Please see [CONTRIBUTING.md](CONTRIBUTING.md).

# Credits

Thank you [Andrei Mărcuţ](https://github.com/andreimarcut) for having the initiative of writing the GUI in Kotlin Multiplatform Compose.
