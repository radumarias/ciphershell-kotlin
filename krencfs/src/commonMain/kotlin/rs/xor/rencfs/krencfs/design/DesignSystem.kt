package rs.xor.rencfs.krencfs.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun KrencfsMaterialDarkTheme(
    colorScheme: ColorScheme = MaterialTheme.colorScheme.copy(
        background = Color.Black,
        onBackground = Color.White,
        surface = Color(0xFF1C1C1C),
        surfaceContainer = Color(0xFF1C1C1C),
        onSurface = Color.White,
        primary = Color(0xFFF5A623),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFF5A623),
        secondary = Color(0xFFF5A623),
        onSecondary = Color.White,
    ),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
