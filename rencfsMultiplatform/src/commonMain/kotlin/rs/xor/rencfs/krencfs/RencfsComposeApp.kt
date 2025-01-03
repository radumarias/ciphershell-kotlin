package rs.xor.rencfs.krencfs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

enum class RencfsScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val showInBottomBar: Boolean = true
) {
    VaultList("vaults", "Vaults", Icons.Filled.Folder),
    VaultDetail("vault/{vaultId}", "Vault Details", Icons.Filled.Folder, false),
    VaultEdit("vault/{vaultId}/edit", "Edit Vault", Icons.Filled.Edit, false),
    Settings("settings", "Settings", Icons.Filled.Settings),
    About("about", "About", Icons.Filled.Info);

    companion object {
        fun vaultDetailRoute(vaultId: String) = "vault/$vaultId"
        fun vaultEditRoute(vaultId: String) = "vault/$vaultId/edit"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RencfsComposeApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = RencfsScreen.entries.find {
        it.route == currentBackStackEntry?.destination?.route
    } ?: RencfsScreen.VaultList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title) },
                navigationIcon = {
                    if (!currentScreen.showInBottomBar) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                },
                actions = {
                    when (currentScreen) {
                        RencfsScreen.VaultDetail -> {
                            IconButton(onClick = {
                                currentBackStackEntry?.arguments?.getString("vaultId")
                                    ?.let { vaultId ->
                                        navController.navigate(RencfsScreen.vaultEditRoute(vaultId))
                                    }
                            }) {
                                Icon(Icons.Filled.Edit, "Edit")
                            }
                        }

                        else -> {}
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                RencfsScreen.entries
                    .filter { it.showInBottomBar }
                    .forEach { screen ->
                        NavigationBarItem(
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
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = RencfsScreen.VaultList.route,
            modifier = Modifier.fillMaxSize().padding(padding),
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