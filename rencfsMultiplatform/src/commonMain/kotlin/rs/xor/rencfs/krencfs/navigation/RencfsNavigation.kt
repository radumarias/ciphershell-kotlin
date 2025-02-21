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
import androidx.compose.material.icons.filled.Settings
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
import rs.xor.rencfs.krencfs.screen.AboutScreen
import rs.xor.rencfs.krencfs.screen.SettingsScreen
import rs.xor.rencfs.krencfs.screen.VaultListScreen
import rs.xor.rencfs.krencfs.screen.VaultViewer
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase
import rs.xor.rencfs.krencfs.ui.components.VaultEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RencfsNavigation(
    navigationController: RencfsNavigationController,
    deviceType: DisplayType,
    vaultListState: VaultListScreenState,
    vaultListUseCase: VaultListScreenUseCase
) {
    val navController = rememberNavController()
    val currentRoute by navigationController.currentRouteFlow.collectAsState()

    // Sync navigation controller with NavController
    navController.addOnDestinationChangedListener { _, destination, _ ->
        val route = when (destination.route) {
            RencfsRoute.VaultList.route -> RencfsRoute.VaultList
            RencfsRoute.VaultCreate.route -> RencfsRoute.VaultCreate
            RencfsRoute.Settings.route -> RencfsRoute.Settings
            RencfsRoute.About.route -> RencfsRoute.About
            else -> RencfsRoute.VaultList // Fallback
        }
        if (route != navigationController.currentRouteFlow.value) {
            navigationController.navigateTo(route)
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            RencfsRoute.entries.forEach { screen ->
                item(
                    icon = { Icon(Icons.Default.Settings, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentRoute == screen,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
                    title = { Text(currentRoute.title) },
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
            val vaultIdArg = navArgument(RencfsRoute.VAULT_ID_PARAM) { type = NavType.StringType }
            NavHost(
                navController = navController,
                startDestination = RencfsRoute.VaultList.route,
                modifier = Modifier.fillMaxSize().padding(padding),
                enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(200)) },
                exitTransition = { ExitTransition.None }
            ) {
                composable(RencfsRoute.VaultList.route) {
                    VaultListScreen(viewState = vaultListState, interactor = vaultListUseCase)
                }
                composable(RencfsRoute.VaultCreate.route) {
                    VaultEditor(createVault = true, onSave = { navController.navigateUp() })
                }
                composable(RencfsRoute.VaultView.BASE_ROUTE, arguments = listOf(vaultIdArg)) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString(RencfsRoute.VAULT_ID_PARAM) ?: return@composable
                    navigationController.navigateTo(RencfsRoute.VaultView(vaultId))
                    VaultViewer(vaultId = vaultId)
                }
                composable(RencfsRoute.VaultEdit.BASE_ROUTE, arguments = listOf(vaultIdArg)) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString(RencfsRoute.VAULT_ID_PARAM) ?: return@composable
                    navigationController.navigateTo(RencfsRoute.VaultEdit(vaultId))
                    VaultEditor(vaultId = vaultId, onSave = { navController.navigateUp() })
                }
                composable(RencfsRoute.Settings.route) { SettingsScreen() }
                composable(RencfsRoute.About.route) { AboutScreen() }
            }
        }
    }
}