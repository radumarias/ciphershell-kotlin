package rs.xor.rencfs.krencfs.design

import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
        surface = Color(0xFF333333),
        surfaceContainer = Color(0xFF1C1C1C),
        onSurface = Color.White,
        primary = Color(0xFFF5A623),
        primaryContainer = Color(0xFFF5A623),
        onPrimary = Color.White,
        secondary = Color(0xFFF5A623),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFF5A623),
        onSecondaryContainer = Color.White,
    ),
    shapes: Shapes = MaterialTheme.shapes.copy(
        extraSmall = RoundedCornerShape(percent = 5),
        small = RoundedCornerShape(percent = 5),
        medium = RoundedCornerShape(percent = 5),
        large = AbsoluteCutCornerShape(percent = 50),
        extraLarge = CutCornerShape(percent = 50),
    ),
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
