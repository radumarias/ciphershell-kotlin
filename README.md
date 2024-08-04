# rencfs-kotlin

## CLI app

It uses the [java-bridge](https://github.com/radumarias/rencfs/tree/main/java-bridge) to interact with Rust code.

### Structure

- [Main.kt](src/main/kotlin/Main.kt): Main file that uses the Rust code.
- [Rust.kt](src/main/kotlin/Rust.kt): Namespace for all the exported Rust functions.

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

You need to have [rencfs](https://github.com/radumarias/rencfs) at the same level with the main project.

```bash
./gradlew cli:build
```

### Run

```bash
./gradlew cli:run
```

This will create the mount mount at `$currentUserHome/rencfs/mnt` and data dir `$currentUserHome/rencfs/data`. Feel free to chnage in [build.gradle.ks](cli/build.gradle.ks).
