# rencfs-kotlin

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

### Exposed functions

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

### Build

```bash
./gradlew cli:build
```

This will also build `java-bridge` for the current OS target.

### Run

It will use the current OS target image for `java-bridge`.

```bash
./gradlew cli:run
```

This will create the mount in `$currentUserHome/rencfs/mnt` and will use as data dir `$currentUserHome/rencfs/data`. Feel free to change in [build.gradle.ks](cli/build.gradle.kts).

## GUI app

### Build

```bash
./gradlew krencfsDesktop:build
```

This will also build `java-bridge` for the current OS target.

### Run

It will use the current OS target image for `java-bridge`.

```bash
./gradlew krencfsDesktop:run
```

# Contribute

Feel free to fork it, change and use it in any way that you want.
If you build something interesting and feel like sharing pull requests are always appreciated.

## How to contribute

Please see [CONTRIBUTING.md](CONTRIBUTING.md).
