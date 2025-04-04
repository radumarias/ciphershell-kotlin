package rs.xor.io.rencfs.krencfs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import rs.xor.rencfs.krencfs.RencfsComposeMainAppContainer
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme

class KrencfsMainLauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

//        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        Log.d("KrencfsMainLauncherActivity", "onCreate")
        setContent {
            RencfsMaterialDarkTheme {
                RencfsComposeMainAppContainer(DisplayType.Phone)
            }
        }
    }
}
