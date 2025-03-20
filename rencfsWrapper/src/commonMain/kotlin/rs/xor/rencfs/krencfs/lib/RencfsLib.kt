package rs.xor.rencfs.krencfs.lib

import hello

abstract class RencfsLib {
    fun rencfsHello(str: String): String = hello(str)

    companion object {
        fun create(): RencfsLib = RencfsLibLoader.loadLib()
    }
}
