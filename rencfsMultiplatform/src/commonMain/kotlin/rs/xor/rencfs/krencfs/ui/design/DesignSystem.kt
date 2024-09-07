package rs.xor.rencfs.krencfs.ui.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val bg_color = Color.Black
val on_bg_color = Color.White

val a_color =  Color(0xFF1C1C1C)
val b_color = Color(0xFFF5A623)

@Composable
fun KrencfsMaterialDarkTheme(
    colorScheme: ColorScheme = MaterialTheme.colorScheme.copy(
        background = bg_color,
        onBackground = on_bg_color,
        surface = a_color,
        surfaceContainer = a_color,
        onSurface = on_bg_color,
        primary = b_color,
        onPrimary = on_bg_color,
        primaryContainer = b_color,
        secondary = b_color,
        onSecondary = on_bg_color,
    ),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
