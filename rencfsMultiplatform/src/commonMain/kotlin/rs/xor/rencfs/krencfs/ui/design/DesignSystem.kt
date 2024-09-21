package rs.xor.rencfs.krencfs.ui.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import rs.xor.rencfs.krencfs.typography.Typography.jetbrainsMono

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
    typography: Typography? = null,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography?: jetbrainsMono().let { JBMono -> MaterialTheme.typography.copy(
            displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = JBMono),
            displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = JBMono),
            displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = JBMono),
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = JBMono),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = JBMono),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = JBMono),
            titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = JBMono),
            titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = JBMono),
            titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = JBMono),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = JBMono),
            bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = JBMono),
            bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = JBMono),
            labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = JBMono),
            labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = JBMono),
            labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = JBMono)
        )
                                                      },
        shapes = shapes,
        content = content,
    )
}
