package rs.xor.rencfs.krencfs.design

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun KrencfsMaterialDarkTheme(
    colors: Colors = darkColors(
        background = Color.Black,
        onBackground = Color.White,
        surface = Color(0xFF1C1C1C),
        onSurface = Color.White,
        primary = Color(0xFFF5A623),
        onPrimary = Color.White,
        primaryVariant = Color(0xFFDEA584),
    ),
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
