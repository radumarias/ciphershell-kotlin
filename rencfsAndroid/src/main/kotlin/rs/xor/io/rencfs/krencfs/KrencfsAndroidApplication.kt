package rs.xor.io.rencfs.krencfs

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import hello
import rs.xor.rencfs.krencfs.RootContextProvider

@HiltAndroidApp
open class KrencfsAndroidApplication : Application() {

    override fun onCreate() {
        RootContextProvider.initialize(applicationContext)
        super.onCreate()
        Log.d(TAG, "onCreate")
        Log.d(TAG, "Hello to Rust, reply: " + hello("Hi from Kotlin"))
    }

    companion object {
        private val TAG: String? = KrencfsAndroidApplication::class.simpleName

        init {
            System.loadLibrary("java_bridge")
        }
    }
}
