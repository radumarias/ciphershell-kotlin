package rs.xor.io.rencfs.krencfs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rs.xor.rencfs.krencfs.DeviceType
import rs.xor.rencfs.krencfs.RencfsComposeAdaptiveApp
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme

class KrencfsMainLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d("KrencfsMainLauncherActivity", "onCreate")
        setContent {
            RencfsMaterialDarkTheme {
                RencfsComposeAdaptiveApp(DeviceType.Phone)
            }
        }
    }
}
