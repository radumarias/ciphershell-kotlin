package rs.xor.io.rencfs.krencfs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rs.xor.rencfs.krencfs.ui.KrencfsMainUI
import rs.xor.rencfs.krencfs.ui.design.KrencfsMaterialDarkTheme

class KrencfsMainLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d("KrencfsMainLauncherActivity", "onCreate")
        setContent {
            KrencfsMaterialDarkTheme {
                KrencfsMainUI()
            }
        }

    }

}
