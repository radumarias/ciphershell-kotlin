package rs.xor.rencfs.krencfs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun KrencfsUI() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize())
        {
            Text("Hello RencfsZ!")
        }
    }
}
