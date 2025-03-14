package rs.xor.io.rencfs.krencfs

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import rs.xor.rencfs.krencfs.RootContextProvider

@HiltAndroidApp
open class KrencfsAndroidApplication : Application() {
    override fun onCreate() {
        RootContextProvider.initialize(applicationContext)
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private val TAG: String? = KrencfsAndroidApplication::class.simpleName
    }
}
