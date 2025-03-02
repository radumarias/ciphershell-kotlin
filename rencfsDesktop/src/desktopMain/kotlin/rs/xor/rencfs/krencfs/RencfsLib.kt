package rs.xor.rencfs.krencfs

import java.io.File

@Suppress("UnsafeDynamicallyLoadedCode")
object RencfsLib {

    init {
        val libPath = resolveLibPath()
        println("Loading library from $libPath")
        System.load(libPath)
        println("Loading successful")
    }

    private fun resolveLibPath() = "${File(".").absolutePath}/libs/${getLibName()}"

    private fun getLibName() = "libjava_bridge.so"

    fun hello(str: String): String {
        return hello(str)
    }
}
