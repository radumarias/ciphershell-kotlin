package rs.xor.rencfs.krencfs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.PlatformNavigation
import rs.xor.rencfs.krencfs.navigation.RencfsRoute
import rs.xor.rencfs.krencfs.screen.SplashScreen
import rs.xor.rencfs.krencfs.screen.usecase.OnCreateVaultUseCase
import rs.xor.rencfs.krencfs.screen.usecase.OnVaultSelectedUseCase
import rs.xor.rencfs.krencfs.screen.usecase.SelectVaultUseCaseParams
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenStateImpl
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCaseImpl

@Composable
fun RencfsComposeMainApp(deviceType: DisplayType) {
    var isLoading by remember { mutableStateOf(true) }
    var count by remember { mutableStateOf(0L) }

    if (isLoading) {
        SplashScreen()
        LaunchedEffect(Unit) {
            val start = System.currentTimeMillis()
            count = SQLDelightDB.getVaultRepositoryAsync().count()
            val loadTime = System.currentTimeMillis() - start
            if (loadTime < 2000) {
                delay(2000 - loadTime)
            }
            isLoading = false
        }
    } else {
        val navController = rememberNavController()
        val vaultListState = remember { VaultListScreenStateImpl(count <= 0) }
        val onCreateVaultUseCase = object : OnCreateVaultUseCase {
            override fun invoke() {
                navController.navigate(RencfsRoute.VaultCreate.route)
            }
        }
        val onVaultSelectedUseCase = object : OnVaultSelectedUseCase {
            override fun invoke(params: SelectVaultUseCaseParams?) {
                params?.vaultId?.let { vaultId ->
                    navController.navigate(RencfsRoute.VaultView.routeWithArgs(vaultId))
                }
            }
        }

        // Instantiate use case
        val vaultListUseCase = VaultListScreenUseCaseImpl(
            onCreateVault = onCreateVaultUseCase,
            onVaultSelected = onVaultSelectedUseCase
        )

        PlatformNavigation.RencfsNavigation(
            navigationController = navController,
            deviceType = deviceType,
            vaultListState = vaultListState,
            vaultListUseCase = vaultListUseCase,
        )
    }
}
