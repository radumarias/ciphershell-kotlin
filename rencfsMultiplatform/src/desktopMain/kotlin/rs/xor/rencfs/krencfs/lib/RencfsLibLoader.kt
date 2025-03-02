package rs.xor.rencfs.krencfs.lib

import java.io.File

actual object RencfsLibLoader {
    actual fun loadLib() = object : RencfsLib() {
        init {
            val libPath = resolveLibPath()
            println("Loading library from $libPath")
            System.load(libPath)
            println("Loading successful")
        }

        private fun resolveLibPath() = "${File(".").absolutePath}/libs/${getLibName()}"

        private fun getLibName() = "libjava_bridge.so"
    }

}
