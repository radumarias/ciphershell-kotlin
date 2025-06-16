package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.customcomponents.RencfsLogoAnimation

@Composable
fun SplashScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingNormal),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            RencfsLogoAnimation()
            GravitationalLogoAnimation()
        }
    }
}
