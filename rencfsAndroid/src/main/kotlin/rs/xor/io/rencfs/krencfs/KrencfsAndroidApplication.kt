package rs.xor.io.rencfs.krencfs

import android.app.Application
import android.util.Log

//@HiltAndroidApp
open class KrencfsAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private val TAG: String? = KrencfsAndroidApplication::class.simpleName
    }
}
