package rs.xor.rencfs.krencfs.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.screen.AboutScreen
import rs.xor.rencfs.krencfs.screen.SettingsScreen
import rs.xor.rencfs.krencfs.screen.VaultListScreen
import rs.xor.rencfs.krencfs.screen.VaultViewer
import rs.xor.rencfs.krencfs.screen.usecase.OnCreateVaultUseCase
import rs.xor.rencfs.krencfs.screen.usecase.OnVaultSelectedUseCase
import rs.xor.rencfs.krencfs.screen.usecase.SelectVaultUseCaseParams
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase
import rs.xor.rencfs.krencfs.ui.components.VaultEditor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RencfsNavigation(deviceType: DisplayType, firstTime: Boolean = false) {
    val navController = rememberNavController()
    val createVaultUseCase = {
        navController.navigate(RencfsRoutes.VaultCreate.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = false
        }
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = RencfsRoutes.entries.find {
        it.route == currentBackStackEntry?.destination?.route
    } ?: RencfsRoutes.VaultList

    NavigationSuiteScaffold(
        modifier = Modifier, navigationSuiteItems = {
            RencfsRoutes.entries.filter { it.isTopLevel }.forEach { screen ->
                item(icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentScreen == screen,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
            when (deviceType) {
                DisplayType.Desktop -> {
                    item(
                        modifier = Modifier.fillMaxHeight(),
                        icon = { Icon(Icons.Filled.Add, contentDescription = "Add folder") },
                        badge = { Spacer(Modifier.fillMaxHeight()) },
                        label = { Text("Add folder") },
                        selected = false,
                        onClick = createVaultUseCase
                    )
                }

                else -> {
                    // TODO
                }
            }
        }, layoutType = when (deviceType) {
            DisplayType.Phone -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.NavigationRail
//         else  -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo() // not yet supported by kmp
        }
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Text(currentScreen.title) }, navigationIcon = {
                if (!currentScreen.isTopLevel) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            }, actions = {
                when (currentScreen) {
                    RencfsRoutes.VaultView -> {
                        IconButton(onClick = {
                            currentBackStackEntry?.arguments?.getString("vaultId")?.let { vaultId ->
                                navController.navigate(
                                    RencfsRoutes.vaultEditRoute(
                                        vaultId
                                    )
                                )
                            }
                        }) {
                            Icon(Icons.Filled.Edit, "Edit")
                        }
                    }

                    else -> {}
                }
            })
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination = RencfsRoutes.VaultList.route,
                modifier = Modifier.fillMaxSize().padding(padding),
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it }, animationSpec = tween(200)
                    )
                },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None },
//            popEnterTransition = { EnterTransition.None },
//            popExitTransition = { ExitTransition.None }
            ) {
                composable(RencfsRoutes.VaultList.route) {
                    VaultListScreen(viewState = @Stable object : VaultListScreenState {
                        init {
                            CoroutineScope(Dispatchers.IO).launch {
                                SQLDelightDB.getVaultRepositoryAsync().observeVaults()
                                    .collectLatest { vaults ->
                                        mutableVaults.value = vaults
                                    }
                            }
                        }

                        val mutableVaults = MutableStateFlow<Map<String, VaultModel>>(emptyMap())
                        override val vaults = mutableVaults.asStateFlow()
                        override val firstStart = firstTime
                    }, interactor = @Stable object : VaultListScreenUseCase {
                        override val onCreateVault: OnCreateVaultUseCase
                            get() = object : OnCreateVaultUseCase {
                                override fun invoke() {
                                    createVaultUseCase()
                                }
                            }
                        override val onVaultSelected: OnVaultSelectedUseCase
                            get() = @Stable object : OnVaultSelectedUseCase {
                                override fun invoke(params: SelectVaultUseCaseParams?) {
                                    navController.navigate(
                                        RencfsRoutes.vaultDetailRoute(
                                            params!!.vaultId!!
                                        )
                                    )
                                }
                            }
                    })
                }
                composable(
                    route = RencfsRoutes.VaultCreate.route,
                ) {
                    VaultEditor(createVault = true, onSave = { navController.navigateUp() })
                }
                composable(
                    route = RencfsRoutes.VaultView.route,
                    arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString("vaultId")
                    VaultViewer(vaultId = vaultId)
                }
                composable(
                    route = RencfsRoutes.VaultEdit.route,
                    arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString("vaultId")
                    VaultEditor(vaultId = vaultId, onSave = { navController.navigateUp() })
                }
                composable(RencfsRoutes.Settings.route) { SettingsScreen() }
                composable(RencfsRoutes.About.route) { AboutScreen() }
            }
        }
    }
}
