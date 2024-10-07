package rs.xor.rencfs.krencfs.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import rs.xor.rencfs.krencfs.ui.branding.DesignSystem


@Composable
fun RencfsMaterialDarkTheme(
    colorScheme: ColorScheme = darkColorScheme(
        background = DesignSystem.Colors.Palette.Primary_Black_900,
        onBackground = Color.White,
        primary = DesignSystem.Colors.Palette.Primary_Orange_600,
        onPrimary = Color.White,
        primaryContainer = DesignSystem.Colors.Palette.Primary_Black_800,
        onPrimaryContainer = DesignSystem.Colors.Palette.Primary_Black_100,
        secondary = DesignSystem.Colors.Palette.Secondary_Brown_600,
        onSecondary = DesignSystem.Colors.Palette.Secondary_Brown_100,
    ),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography? = null,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography ?: MaterialTheme.typography,
        shapes = shapes,
        content = content,
    )
}
