package rs.xor.rencfs.krencfs


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import rs.xor.rencfs.krencfs.design.KrencfsMaterialDarkTheme

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    Tray(
        icon = rememberVectorPainter(Icons.Default.Lock),
        menu = {
            Menu("Main Menu") {
                Item("First Option") { TODO(/*CLICKED*/) }
                Item("Second Option") { TODO(/*CLICKED*/) }
            }
            Separator()
            Menu( "Quit?") {
                Item(
                    "Quit Right Now!",
                    onClick = { exitApplication() }
                )
            }
        }
    )
    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = "Krencfs",
        ) {
            KrencfsMaterialDarkTheme {
                KrencfsUI()
            }
        }
    }
}
