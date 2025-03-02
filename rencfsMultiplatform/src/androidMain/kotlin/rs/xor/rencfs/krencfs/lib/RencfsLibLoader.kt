package rs.xor.rencfs.krencfs.lib

actual object RencfsLibLoader {
    actual fun loadLib(): RencfsLib = object : RencfsLib() {
        init {
            println("Loading library libjava_bridge.so")
            System.loadLibrary("java_bridge")
            println("Loading successful")
        }
    }
}