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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.About
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.Settings
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultCreate
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultList
import rs.xor.rencfs.krencfs.screen.AboutScreen
import rs.xor.rencfs.krencfs.screen.SettingsScreen
import rs.xor.rencfs.krencfs.screen.VaultListScreen
import rs.xor.rencfs.krencfs.screen.VaultViewer
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.VaultSetupFlow
import rs.xor.rencfs.krencfs.ui.components.VaultEditor

actual object PlatformNavigation {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    actual fun RencfsNavigation(
        navigationController: NavHostController,
        deviceType: DisplayType,
        firstTime: Boolean,
    ) {
        val currentRoute =
            navigationController.currentBackStack.collectAsState(null).value?.lastOrNull()
                ?.let { backStackEntry ->
                    RencfsRoute.fromRoute(
                        backStackEntry.destination.route,
                        backStackEntry.arguments,
                    )
                } ?: VaultList

        println("PlatformNavigation.RencfsNavigation currentRoute: $currentRoute")

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                listOf(VaultList, Settings, About).forEach { screen ->
                    item(
                        icon = {
                            Icon(
                                screen.mapToIcon(),
                                contentDescription = screen.mapToTitle(),
                            )
                        },
                        label = { Text(screen.mapToTitle()) },
                        selected = currentRoute == screen,
                        onClick = { navigationController.navigate(screen.route) },
                    )
                }
                if (deviceType == DisplayType.Desktop) {
                    item(
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Add folder",
                            )
                        },
                        label = { Text("Add folder") },
                        selected = false,
                        onClick = { navigationController.navigate(VaultCreate.route) },
                    )
                }
            },
            layoutType = when (deviceType) {
                DisplayType.Phone -> NavigationSuiteType.NavigationBar
                else -> NavigationSuiteType.NavigationRail
            },
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentRoute.mapToTitle()) },
                        navigationIcon = {
                            if (!currentRoute.isTopLevel) {
                                IconButton(onClick = { navigationController.navigateUp() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                                }
                            }
                        },
                        actions = {
                            if (currentRoute is RencfsRoute.VaultView) {
                                IconButton(onClick = {
                                    navigationController.navigate(RencfsRoute.VaultEdit(currentRoute.vaultId).route)
                                }) {
                                    Icon(Icons.Filled.Edit, "Edit")
                                }
                            }
                        },
                    )
                },
            ) { padding ->

                NavHost(
                    navController = navigationController,
                    startDestination = VaultList.route,
                    modifier = Modifier.fillMaxSize().padding(padding),
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(200),
                        )
                    },
                    exitTransition = { ExitTransition.None },
                ) {
                    val vaultId = navArgument(RencfsRoute.VAULT_PARAM_ID) {
                        type = NavType.StringType
                    }

                    composable(VaultList.route) {
                        VaultListScreen(firstTime)
                    }
                    composable(VaultCreate.route) {
                        VaultSetupFlow(
                            isDesktop = deviceType == DisplayType.Desktop,
                            onViewDashboard = { navigationController.navigateUp() },
                            onUnlockFolder = {},
                        )
//                        TODO cleanup Vault editor
//                        VaultEditor(
//                            createVault = true,
//                            onSave = { navigationController.navigateUp() },
//                        )
                    }
                    composable(
                        RencfsRoute.VaultView.BASE_ROUTE,
                        arguments = listOf(vaultId),
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString(RencfsRoute.VAULT_PARAM_ID)
                            ?.let { vaultId ->
                                VaultViewer(vaultId = vaultId)
                            } ?: run {
                            println("Vault ID not found: $vaultId")
                            navigationController.navigateUp()
                        }
                    }
                    composable(
                        RencfsRoute.VaultEdit.BASE_ROUTE,
                        arguments = listOf(vaultId),
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString(RencfsRoute.VAULT_PARAM_ID)
                            ?.let { vaultId ->
//                        TODO cleanup Vault editor
                                VaultEditor(
                                    vaultId = vaultId,
                                    onSave = { navigationController.navigateUp() },
                                )
                            } ?: run {
                            println("Vault ID not found: vaultId")
                            navigationController.navigateUp() //
                        }
                    }
                    composable(Settings.route) { SettingsScreen() }
                    composable(About.route) { AboutScreen() }
                }
            }
        }
    }
}
