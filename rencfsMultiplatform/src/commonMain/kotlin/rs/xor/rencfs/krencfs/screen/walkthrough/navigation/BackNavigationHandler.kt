package rs.xor.rencfs.krencfs.screen.walkthrough.navigation

import androidx.compose.runtime.Composable

expect class BackNavigationHandler {
    @Composable
    fun OnBackPressed(callback: () -> Unit)
}

expect fun provideBackNavigationHandler(): BackNavigationHandler
