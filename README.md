# rencfs-kotlin

Kotlin GUI app for [rencfs](https://github.com/radumarias/rencfs).

It uses the [java-bridge](https://github.com/radumarias/rencfs/tree/main/java-bridge) to interact with Rust code.

## Structure

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

## Build

You need to have [rencfs](https://github.com/radumarias/rencfs) at the same level.

```bash
cd ../rencfs/java-bridge
make
cd -
kotlinc src/main/kotlin/Main.kt src/main/kotlin/Rust.kt -include-runtime -d build/libs/rencfs-kotlin.jar
```

## Run

```bash
java -Djava.library.path=../rencfs/java-bridge/target/release/ -jar build/libs/rencfs-kotlin.jar /home/gnome/rencfs /home/gnome/rencfs_data a
```
