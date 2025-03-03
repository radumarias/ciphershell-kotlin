package rs.xor.rencfs.krencfs


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme


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
                RencfsComposeMainApp(DisplayType.Desktop)
            }
        }
    }
}
