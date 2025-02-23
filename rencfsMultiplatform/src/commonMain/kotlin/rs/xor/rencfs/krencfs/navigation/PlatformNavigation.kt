package rs.xor.rencfs.krencfs.navigation

import androidx.compose.runtime.Composable
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase

expect object PlatformNavigation {
    @Composable
    fun RencfsNavigation(
        navigationController: RencfsNavigationController,
        deviceType: DisplayType,
        vaultListState: VaultListScreenState,
        vaultListUseCase: VaultListScreenUseCase
    )
}
