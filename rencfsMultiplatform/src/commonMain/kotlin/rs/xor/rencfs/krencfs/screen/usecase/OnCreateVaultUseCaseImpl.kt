package rs.xor.rencfs.krencfs.screen.usecase

import androidx.navigation.NavController
import rs.xor.rencfs.krencfs.navigation.RencfsRoute

class OnCreateVaultUseCaseImpl(
    private val navController: NavController
) : OnCreateVaultUseCase {
    override fun invoke() {
        navController.navigate(RencfsRoute.VaultCreate.route)
    }
}
