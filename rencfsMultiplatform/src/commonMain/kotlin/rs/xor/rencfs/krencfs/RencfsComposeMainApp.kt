package rs.xor.rencfs.krencfs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.di.vaultModule
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.PlatformNavigation
import rs.xor.rencfs.krencfs.screen.SplashScreen

@Composable
fun RencfsComposeMainAppContainer(deviceType: DisplayType) {
    val vaultRepository = remember { get().get<VaultRepository>() }
    RencfsComposeMainApp(deviceType, vaultRepository)
}

@Composable
fun RencfsComposeMainApp(
    deviceType: DisplayType,
    vaultRepository: VaultRepository,
) {
//    val rencfsLib = RencfsLib.create()
//    println("RencfsComposeMainApp called" + rencfsLib.rencfsHello("Hi from Multiplatform!"))
    var isLoading by remember { mutableStateOf(true) }
    var count by remember { mutableStateOf(0L) }

    if (isLoading) {
        SplashScreen()
        LaunchedEffect(Unit) {
            val start = System.currentTimeMillis()
            count = vaultRepository.count()
            val loadTime = System.currentTimeMillis() - start
            if (loadTime < 2000) {
                delay(2000 - loadTime)
            }
            isLoading = false
        }
    } else {
        val navController = rememberNavController()

        // Load navigation-dependent modules
        loadKoinModules(
            listOf(
                vaultModule,
                module { single { navController } },
            ),
        )
        val firstTime = count <= 0
        PlatformNavigation.RencfsNavigation(
            navigationController = navController,
            deviceType = deviceType,
            firstTime = firstTime,
        )
    }
}
