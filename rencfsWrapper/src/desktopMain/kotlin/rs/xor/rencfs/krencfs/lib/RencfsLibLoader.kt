package rs.xor.rencfs.krencfs.lib

import java.io.File

@Suppress("UnsafeDynamicallyLoadedCode")
actual object RencfsLibLoader {
    actual fun loadLib() = object : RencfsLib() {
        init {
            val libPath = resolveLibPath()
            println("Loading library from $libPath")
            System.load(libPath)
            println("Loading successful")
        }

        private fun resolveLibPath() = "${File(".").absolutePath}/libs/${getLibName()}"

        private fun getLibName() = "libjava_bridge.${detectLibraryExtension()}"

        fun detectLibraryExtension(): String {
            val os: String = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> "dll"
                os.contains("mac") -> "dylib"
                else -> "so"
            }
        }
    }
}
