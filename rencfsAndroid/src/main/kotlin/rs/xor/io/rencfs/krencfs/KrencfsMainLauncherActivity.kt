package rs.xor.io.rencfs.krencfs

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
//import rs.xor.rencfs.krencfs.ui.design.KrencfsMaterialDarkTheme

class KrencfsMainLauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState, persistentState)
        Log.d("KrencfsMainLauncherActivity", "onCreate")
        setContent {
            MaterialTheme {
                Surface  (modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Hello, Android! ",
                            color = Color.Blue,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
