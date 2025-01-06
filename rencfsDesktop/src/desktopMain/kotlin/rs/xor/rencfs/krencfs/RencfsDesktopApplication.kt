package rs.xor.rencfs.krencfs


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.*
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme
import rs.xor.rencfs.krencfs.display.DisplayType

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    Tray(
        icon = rememberVectorPainter(Icons.Default.Lock),
        onAction = { isOpen = true },
        menu = {
            Item("Exit", onClick = ::exitApplication)
        },
    )

    if (isOpen) {
        Window(
            undecorated = false,
            onCloseRequest = { isOpen = false },
            title = "Krencfs",
            state = rememberWindowState(
                placement = WindowPlacement.Floating,
//                width = Dp.Unspecified,
//                height = Dp.Unspecified,
            ),
        ) {
            MenuBar {
                Menu("File") {
                    Item("Exit", onClick = ::exitApplication)
                }
            }
            RencfsMaterialDarkTheme {
                RencfsComposeAdaptiveApp(DisplayType.Desktop)
            }
        }
    }
}
