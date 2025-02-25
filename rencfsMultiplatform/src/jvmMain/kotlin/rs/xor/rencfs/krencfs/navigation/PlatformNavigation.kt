package rs.xor.rencfs.krencfs.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.About
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.Settings
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultList
import rs.xor.rencfs.krencfs.screen.AboutScreen
import rs.xor.rencfs.krencfs.screen.SettingsScreen
import rs.xor.rencfs.krencfs.screen.VaultListScreen
import rs.xor.rencfs.krencfs.screen.VaultViewer
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase
import rs.xor.rencfs.krencfs.ui.components.VaultEditor

actual object PlatformNavigation {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    actual fun RencfsNavigation(
        navigationController: RencfsNavigationController,
        topDestinations: List<RencfsRoute>,
        deviceType: DisplayType,
        vaultListState: VaultListScreenState,
        vaultListUseCase: VaultListScreenUseCase
    ) {
        val navController = rememberNavController()
        val currentRoute by navigationController.currentRouteFlow.collectAsState()

        // Sync navigation controller with NavController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val route = when (destination.route) {
                VaultList.route -> VaultList
                RencfsRoute.VaultCreate.route -> RencfsRoute.VaultCreate
                Settings.route -> Settings
                About.route -> About
                else -> VaultList // Fallback
            }
            if (route != navigationController.currentRouteFlow.value) {
                navigationController.navigateTo(route)
            }
        }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                topDestinations.forEach { screen ->
                    item(
                        icon = {
                            Icon(
                                screen.mapToIcon(),
                                contentDescription = screen.mapToTitle()
                            )
                        },
                        label = { Text(screen.mapToTitle()) },
                        selected = currentRoute == screen,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                if (deviceType == DisplayType.Desktop) {
                    item(
                        icon = { Icon(Icons.Filled.Add, contentDescription = "Add folder") },
                        label = { Text("Add folder") },
                        selected = false,
                        onClick = { navController.navigate(RencfsRoute.VaultCreate.route) }
                    )
                }
            },
            layoutType = when (deviceType) {
                DisplayType.Phone -> NavigationSuiteType.NavigationBar
                else -> NavigationSuiteType.NavigationRail
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentRoute.mapToTitle()) },
                        navigationIcon = {
                            if (!currentRoute.isTopLevel) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                                }
                            }
                        },
                        actions = {
                            if (currentRoute is RencfsRoute.VaultView) {
                                IconButton(onClick = {
                                    navController.navigate(
                                        RencfsRoute.VaultEdit.routeWithArgs((currentRoute as RencfsRoute.VaultView).vaultId)
                                    )
                                }) {
                                    Icon(Icons.Filled.Edit, "Edit")
                                }
                            }
                        }
                    )
                }
            ) { padding ->

                NavHost(
                    navController = navController,
                    startDestination = VaultList.route,
                    modifier = Modifier.fillMaxSize().padding(padding),
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(200)
                        )
                    },
                    exitTransition = { ExitTransition.None }
                ) {
                    val vaultId = navArgument(RencfsRoute.VAULT_ID_PARAM) {
                        type = NavType.StringType
                    }

                    composable(VaultList.route) {
                        VaultListScreen(viewState = vaultListState, interactor = vaultListUseCase)
                    }
                    composable(RencfsRoute.VaultCreate.route) {
                        VaultEditor(createVault = true, onSave = { navController.navigateUp() })
                    }
                    composable(
                        RencfsRoute.VaultView.BASE_ROUTE,
                        arguments = listOf(vaultId)
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString(RencfsRoute.VAULT_ID_PARAM)?.apply {
                            navigationController.navigateTo(RencfsRoute.VaultView(this))
                            VaultViewer(vaultId = this)
                        } ?: run {
                            println("Vault ID not found")
                            navController.navigateUp()
                        }
                    }
                    composable(
                        RencfsRoute.VaultEdit.BASE_ROUTE,
                        arguments = listOf(vaultId)
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString(RencfsRoute.VAULT_ID_PARAM)?.apply {
                            navigationController.navigateTo(RencfsRoute.VaultEdit(this))
                            VaultEditor(vaultId = this, onSave = { navController.navigateUp() })
                        } ?: run {
                            println("Vault ID not found")
                            navController.navigateUp()
                        }
                    }
                    composable(Settings.route) { SettingsScreen() }
                    composable(About.route) { AboutScreen() }
                }
            }
        }
    }
}