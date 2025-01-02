package rs.xor.rencfs.krencfs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.xor.rencfs.krencfs.ui.components.VaultEditor
import rs.xor.rencfs.krencfs.ui.screens.AboutScreen
import rs.xor.rencfs.krencfs.ui.screens.SettingsScreen
import rs.xor.rencfs.krencfs.ui.screens.VaultListScreen
import rs.xor.rencfs.krencfs.ui.screens.VaultViewer

enum class DeviceType {
    Phone,      // Handheld, small-screen devices
    Tablet,     // Larger handheld devices
    Laptop,     // Portable computers with keyboards
    Desktop,    // Stationary computers
    TV,          // Large-screen devices
    // ETC.
}

@Composable
fun RencfsComposeAdaptiveApp(deviceType: DeviceType) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = RencfsScreen.entries.find {
        it.route == currentBackStackEntry?.destination?.route
    } ?: RencfsScreen.VaultList
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            RencfsScreen.entries
                .filter { it.showInBottomBar }
                .forEach { screen ->
                    item(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
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
                        }
                    )
                }
        },
        layoutType = when (deviceType) {
                DeviceType.Phone -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.NavigationRail
//         else  -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo() // not yet supported by kmp
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = RencfsScreen.VaultList.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200)
                )
            },
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None },
//            popEnterTransition = { EnterTransition.None },
//            popExitTransition = { ExitTransition.None }
        ) {
            composable(RencfsScreen.VaultList.route) {
                VaultListScreen(
                    onVaultSelected = { vaultId ->
                        navController.navigate(RencfsScreen.vaultDetailRoute(vaultId))
                    }
                )
            }
            composable(
                route = RencfsScreen.VaultDetail.route,
                arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vaultId = backStackEntry.arguments?.getString("vaultId")
                VaultViewer(vaultId = vaultId)
            }
            composable(
                route = RencfsScreen.VaultEdit.route,
                arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vaultId = backStackEntry.arguments?.getString("vaultId")
                VaultEditor(
                    vaultId = vaultId,
                    onSave = { navController.navigateUp() }
                )
            }
            composable(RencfsScreen.Settings.route) { SettingsScreen() }
            composable(RencfsScreen.About.route) { AboutScreen() }
        }
    }
}