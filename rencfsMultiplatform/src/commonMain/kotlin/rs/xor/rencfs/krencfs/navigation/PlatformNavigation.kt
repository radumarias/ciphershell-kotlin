package rs.xor.rencfs.krencfs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import rs.xor.rencfs.krencfs.display.DisplayType

expect object PlatformNavigation {
    @Composable
    fun RencfsNavigation(
        navigationController: NavHostController,
        deviceType: DisplayType,
        firstTime: Boolean,
    )
}
