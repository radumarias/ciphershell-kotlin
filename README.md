# ciphershell-kotlin

[![ci](https://github.com/radumarias/rencfs-kotlin/actions/workflows/ci.yml/badge.svg)](https://github.com/radumarias/rencfs-kotlin/actions/workflows/ci.yml)
[![codetriage](https://www.codetriage.com/radumarias/rencfs-kotlin/badges/users.svg)](https://www.codetriage.com/radumarias/rencfs-kotlin)

GUI for [rencfs](https://github.com/radumarias/rencfs) in Koltin Multiplatform with Compose.

It uses the [java-bridge](https://github.com/radumarias/rencfs/tree/main/java-bridge) to interact with Rust code.

> [!WARNING]  
> **This is still under development. Please do not use it with sensitive data for now, please wait for a
stable release.  
> It's mostly ideal for experimental and learning projects. It serves as a reference app for a GUI for `rencfs`.**

## CLI test app

### Structure

- [Main.kt](cli/src/main/kotlin/Main.kt): Main file that uses the Rust code.
- [Rust.kt](cli/src/main/kotlin/Rust.kt): Namespace for all the exported Rust functions.

### Exposed rencfs api

- `fun hello(str: String): String;`: Test function that takes a string and returns it with some additional one.
- `fun mount(mnt: String, dataDir: String, password: String, umountFirst: Bolean): Int;`: Mounts a filesystem at `mnt`
  with `dataDir`
  and `password`, returning the mount handle.umount_first: If true, unmounts the filesystem at `mnt` before mounting.  
  **@param** `umount_first`: If `true`, unmounts the filesystem at `mnt` before mounting.
- `fun umount(handle: Int);`: Unmounts the filesystem at `mount handle` returned by [mount].
- `fun umountAll();`: Unmounts all mounted filesystems.
- ```text
  fun state(
  dryRun: Boolean = false,
  simulateMountError: Boolean = false,
  simulateUmountError: Boolean = false,
  simulateUmountAllError: Boolean = false,
  );
  ```
  Set app state. Helpful to simulate various errors and `dry-run`.

### Building rencfs command line

```bash
./gradlew cli:build
```

This will also build `java-bridge` for the current OS target.

### Run rencfs command line 

It will use the current OS target image for `java-bridge`.

```bash
./gradlew cli:run
```

This will create the mount in `$currentUserHome/rencfs/mnt` and will use as data dir `$currentUserHome/rencfs/data`. Feel free to change in [build.gradle.ks](cli/build.gradle.kts).

## Graphical User Interface Application (GUI app)

### Integrated Development Environment (IDE)

The multiplatform project is developed mainly using IntelliJ Ultimate or JetBrains Fleet IDE. 

For Android development only, you can use Android Studio version Ladybug 2024.2.1. 

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

#### WARNING: K2 is not supported. Don't enable it.

The latest Android Studio IDE will ask you to enable K2 (because we are using Kotlin 2.0.0+) but it is not working out of the box.
Don't enable it, you could encounter unexpected issues.

We can enable it when in the future, asuming it doesn't break the IDE forever.

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
