package rs.xor.io.rencfs.krencfs

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import rs.xor.rencfs.krencfs.ui.KrencfsMainUI
import rs.xor.rencfs.krencfs.ui.design.KrencfsMaterialDarkTheme

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            KrencfsMaterialDarkTheme {
                KrencfsMainUI()
            }
        }
    }
}
