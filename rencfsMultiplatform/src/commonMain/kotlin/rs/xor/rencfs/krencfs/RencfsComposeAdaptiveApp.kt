package rs.xor.rencfs.krencfs

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.RencfsNavigationRoutes
import rs.xor.rencfs.krencfs.screen.AboutScreen
import rs.xor.rencfs.krencfs.screen.SettingsScreen
import rs.xor.rencfs.krencfs.screen.SplashScreen
import rs.xor.rencfs.krencfs.screen.VaultListScreen
import rs.xor.rencfs.krencfs.screen.VaultViewer
import rs.xor.rencfs.krencfs.ui.components.VaultEditor

@Composable
fun RencfsComposeAdaptiveApp(deviceType: DisplayType) {
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
        RencfsComposeAdaptiveApp2(deviceType, count <= 0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RencfsComposeAdaptiveApp2(deviceType: DisplayType, firstTime: Boolean = false) {
    val navController = rememberNavController()
    val createVaultUseCase = {
        navController.navigate(RencfsNavigationRoutes.VaultCreate.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = false
        }
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = RencfsNavigationRoutes.entries.find {
        it.route == currentBackStackEntry?.destination?.route
    } ?: RencfsNavigationRoutes.VaultList

    NavigationSuiteScaffold(
        modifier = Modifier,
        navigationSuiteItems = {
            RencfsNavigationRoutes.entries
                .filter { it.isTopLevel }
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
            when (deviceType) {
                DisplayType.Desktop -> {
                    item(
                        modifier = Modifier
                            .fillMaxHeight(),
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
        },
        layoutType = when (deviceType) {
            DisplayType.Phone -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.NavigationRail
//         else  -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo() // not yet supported by kmp
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen.title) },
                    navigationIcon = {
                        if (!currentScreen.isTopLevel) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        }
                    },
                    actions = {
                        when (currentScreen) {
                            RencfsNavigationRoutes.VaultView -> {
                                IconButton(onClick = {
                                    currentBackStackEntry?.arguments?.getString("vaultId")
                                        ?.let { vaultId ->
                                            navController.navigate(
                                                RencfsNavigationRoutes.vaultEditRoute(
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
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = RencfsNavigationRoutes.VaultList.route,
                modifier = Modifier.fillMaxSize().padding(padding),
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(200)
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
                composable(RencfsNavigationRoutes.VaultList.route) {
                    VaultListScreen(
                        firstStart = firstTime,
                        onVaultSelected = { vaultId ->
                            navController.navigate(RencfsNavigationRoutes.vaultDetailRoute(vaultId))
                        },
                        onCreateVault = createVaultUseCase
                    )
                }
                composable(
                    route = RencfsNavigationRoutes.VaultCreate.route,
                ) {
                    VaultEditor(
                        createVault = true,
                        onSave = { navController.navigateUp() }
                    )
                }
                composable(
                    route = RencfsNavigationRoutes.VaultView.route,
                    arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString("vaultId")
                    VaultViewer(vaultId = vaultId)
                }
                composable(
                    route = RencfsNavigationRoutes.VaultEdit.route,
                    arguments = listOf(navArgument("vaultId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val vaultId = backStackEntry.arguments?.getString("vaultId")
                    VaultEditor(
                        vaultId = vaultId,
                        onSave = { navController.navigateUp() }
                    )
                }
                composable(RencfsNavigationRoutes.Settings.route) { SettingsScreen() }
                composable(RencfsNavigationRoutes.About.route) { AboutScreen() }
            }
        }
    }
}