package rs.xor.rencfs.krencfs.lib

import hello

abstract class RencfsLib {
    fun rencfsHello(str: String): String {
        return hello(str)
    }

    companion object {
        fun create(): RencfsLib {
            return RencfsLibLoader.loadLib()
        }
    }
}