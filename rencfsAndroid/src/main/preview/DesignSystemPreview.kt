import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.ui.branding.DesignSystem
import rs.xor.rencfs.krencfs.ui.branding.backgroundAngularGradient

@Preview(widthDp = 200, heightDp = 100)
@Composable
fun Preview_Oxidized_Iron() {
    Box(modifier = Modifier
        .backgroundAngularGradient(DesignSystem.Colors.Gradient.Oxidized_Iron)
        .fillMaxSize()
    )
}

@Preview(widthDp = 200, heightDp = 100)
@Composable
fun Preview_Oxidized_Copper() {
    Box(modifier = Modifier
        .backgroundAngularGradient(DesignSystem.Colors.Gradient.Oxidized_Copper)
        .fillMaxSize()
    )
}
