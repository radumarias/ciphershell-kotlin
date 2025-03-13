package rs.xor.rencfs.krencfs.ui.design

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(widthDp = 280, heightDp = 100)
@Composable
fun Preview_Oxidized_Iron() {
    Box(
        modifier = Modifier
            .backgroundAngularGradient(DesignSystem.Colors.Gradient.Oxidized_Iron)
            .fillMaxSize(),
    )
}

@Preview(widthDp = 280, heightDp = 100)
@Composable
fun Preview_Oxidized_Copper() {
    Box(
        modifier = Modifier
            .backgroundAngularGradient(DesignSystem.Colors.Gradient.Oxidized_Copper)
            .fillMaxSize(),
    )
}
