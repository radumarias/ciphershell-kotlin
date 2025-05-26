package rs.xor.io.rencfs.krencfs

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import rs.xor.rencfs.krencfs.di.androidModule
import rs.xor.rencfs.krencfs.di.sqlDelightModule

open class KrencfsAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        startKoin {
            Log.d(TAG, "startKoin")
            androidContext(this@KrencfsAndroidApplication)
            modules(sqlDelightModule, androidModule)
        }
    }

    companion object {
        private val TAG: String? = KrencfsAndroidApplication::class.simpleName
    }
}
