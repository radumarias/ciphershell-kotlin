# rencfs-kotlin

Kotlin GUI for rencfs.

- [kotlin-bridge](kotlin-bridge) builds the `librust_jni.so` in [release](kotlin-bridge/target/release) dir. That folder you need to provide when running Kotlin app.
- [kotlin](kotlin) is an example app that uses the bridge to call Rust code. It has an example for a normal flow and several simulations of errors and dry-run.