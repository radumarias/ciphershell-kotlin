package rs.xor.rencfs.krencfs.screen.usecase

import androidx.navigation.NavController
import rs.xor.rencfs.krencfs.navigation.RencfsRoute

class OnVaultSelectedUseCaseImpl (
    private val navController: NavController
): OnVaultSelectedUseCase {
    override fun invoke(params: SelectVaultUseCaseParams?) {
        params?.vaultId?.let { vaultId ->
            navController.navigate(RencfsRoute.VaultView.routeWithArgs(vaultId))
        }
    }
}
