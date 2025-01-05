package rs.xor.rencfs.krencfs.ui.design

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


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
        typography = typography ?: DesignSystem.Typography.JetBrainsMono().let { JBMono ->
            MaterialTheme.typography.copy(
                displayLarge = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = JBMono,
                    fontSize = 110.sp
                ),
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
