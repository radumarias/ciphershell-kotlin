package rs.xor.rencfs.krencfs.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RencfsNavigationController(initialRoute: RencfsRoute) {
    private val _currentRouteFlow = MutableStateFlow(initialRoute)
    val currentRouteFlow: StateFlow<RencfsRoute> = _currentRouteFlow.asStateFlow()

    fun navigateTo(route: RencfsRoute) {
        _currentRouteFlow.value = route
    }

    fun navigateUp() {
        val current = _currentRouteFlow.value
        _currentRouteFlow.value = when {
            current.isTopLevel -> current
            else -> RencfsRoute.VaultList
        }
    }
}