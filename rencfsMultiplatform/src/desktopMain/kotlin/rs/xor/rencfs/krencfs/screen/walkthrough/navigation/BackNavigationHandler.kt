package rs.xor.rencfs.krencfs.screen.walkthrough.navigation

import androidx.compose.runtime.Composable

actual class BackNavigationHandler {
    @Composable
    actual fun OnBackPressed(callback: () -> Unit) {
        // No op, Desktop handles back navigation via UI button
    }
}

actual fun provideBackNavigationHandler(): BackNavigationHandler = BackNavigationHandler()
