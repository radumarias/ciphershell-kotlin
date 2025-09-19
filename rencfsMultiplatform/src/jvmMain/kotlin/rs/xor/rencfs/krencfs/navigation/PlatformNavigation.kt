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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
                    try {
                        when (backStackEntry.destination.route) {
                            "rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultList" -> RencfsRoute.VaultList
                            "rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultCreate" -> RencfsRoute.VaultCreate
                            "rs.xor.rencfs.krencfs.navigation.RencfsRoute.Settings" -> RencfsRoute.Settings
                            "rs.xor.rencfs.krencfs.navigation.RencfsRoute.About" -> RencfsRoute.About
                            else -> {
                                if (backStackEntry.destination.route?.contains("VaultView") == true) {
                                    backStackEntry.toRoute<RencfsRoute.VaultView>()
                                } else if (backStackEntry.destination.route?.contains("VaultEdit") == true) {
                                    backStackEntry.toRoute<RencfsRoute.VaultEdit>()
                                } else {
                                    RencfsRoute.VaultList
                                }
                            }
                        }
                    } catch (e: Exception) {
                        RencfsRoute.VaultList
                    }
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
                        onClick = { navigationController.navigate(screen) },
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
                        onClick = { navigationController.navigate(VaultCreate) },
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
                                    navigationController.navigate(RencfsRoute.VaultEdit(currentRoute.vaultId))
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
                    startDestination = VaultList,
                    modifier = Modifier.fillMaxSize().padding(padding),
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(200),
                        )
                    },
                    exitTransition = { ExitTransition.None },
                ) {

                    composable<RencfsRoute.VaultList> {
                        VaultListScreen(firstTime)
                    }
                    composable<RencfsRoute.VaultCreate> {
                        VaultSetupFlow(
                            vaultId = null,
                            isEditMode = false,
                            isDesktop = deviceType == DisplayType.Desktop,
                            onViewDashboard = { navigationController.navigateUp() },
                        )
                    }
                    composable<RencfsRoute.VaultView> { backStackEntry ->
                        val vaultView: RencfsRoute.VaultView = backStackEntry.toRoute()
                        VaultViewer(vaultId = vaultView.vaultId)
                    }
                    composable<RencfsRoute.VaultEdit> { backStackEntry ->
                        val vaultEdit: RencfsRoute.VaultEdit = backStackEntry.toRoute()
                        VaultSetupFlow(
                            vaultId = vaultEdit.vaultId,
                            isEditMode = true,
                            isDesktop = deviceType == DisplayType.Desktop,
                            onViewDashboard = { navigationController.navigateUp() },
                        )
                    }
                    composable<RencfsRoute.Settings> { SettingsScreen() }
                    composable<RencfsRoute.About> { AboutScreen() }
                }
            }
        }
    }
}
