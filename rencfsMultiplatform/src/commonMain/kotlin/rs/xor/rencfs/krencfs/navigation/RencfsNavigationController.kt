package rs.xor.rencfs.krencfs.navigation

import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.About
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.Settings
import rs.xor.rencfs.krencfs.navigation.RencfsRoute.VaultList

class RencfsNavigationController(
    initialRoute: RencfsRoute,
    val navController: NavHostController, // Required, not optional
    private val onRouteChanged: (RencfsRoute, String, Boolean) -> Unit
) {
    private val _stack = MutableStateFlow(listOf(initialRoute))
    private val _currentRoute = MutableStateFlow(initialRoute)
    val currentRouteFlow: StateFlow<RencfsRoute> = _currentRoute.asStateFlow()

    init {
        // Listen to navController changes (e.g., system back)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val newRoute = listOf(VaultList, Settings, About).find { it.route == destination.route }
            if (newRoute != null && newRoute != _currentRoute.value) {
                // Sync internal stack on back navigation
                if (_stack.value.size > 1 && _stack.value.last() != newRoute) {
                    _stack.value = _stack.value.dropLast(1)
                    _currentRoute.value = newRoute
                    println("ZZZZ - Synced from navController: Stack size=${_stack.value.size}, Route=${newRoute.route}")
                    onRouteChanged(newRoute, newRoute.route, true)
                }
            }
        }
    }

    fun navigateTo(route: RencfsRoute) {
        _stack.value = buildList {
            addAll(_stack.value)
            if (lastOrNull()?.route != route.route) {
                add(route)
            }
        }
        _currentRoute.value = route
        val resolvedRoute = when (route) {
            is RencfsRoute.VaultView -> RencfsRoute.VaultView.routeWithArgs(route.vaultId)
            is RencfsRoute.VaultEdit -> RencfsRoute.VaultEdit.routeWithArgs(route.vaultId)
            else -> route.route
        }
        println("ZZZZ - NavigateTo: Stack size=${_stack.value.size}, Route=$resolvedRoute")
        onRouteChanged(route, resolvedRoute, false)
        navController.navigate(resolvedRoute)
    }

    fun navigateUp() {
        println("ZZZZ - NavigateUp called: Stack size=${_stack.value.size}, Current stack=${_stack.value.map { it.route }}")
        if (_stack.value.size > 1) {
            _stack.value = _stack.value.dropLast(1)
            val newRoute = _stack.value.last()
            _currentRoute.value = newRoute
            println("ZZZZ - NavigateUp: Setting currentRoute to ${newRoute.route}")
            onRouteChanged(newRoute, newRoute.route, true)
            navController.popBackStack()
        } else {
            println("ZZZZ - NavigateUp: Stack size <= 1, forcing to VaultList")
            val newRoute = RencfsRoute.VaultList
            _currentRoute.value = newRoute
            onRouteChanged(newRoute, newRoute.route, true)
            navController.navigate(RencfsRoute.VaultList.route)
        }
    }

    // Handle back press (system or UI)
    fun handleBack(): Boolean {
        return if (_currentRoute.value != RencfsRoute.VaultList) {
            navigateUp()
            true // Back handled
        } else {
            false // Let system handle (e.g., exit app on Android)
        }
    }
}