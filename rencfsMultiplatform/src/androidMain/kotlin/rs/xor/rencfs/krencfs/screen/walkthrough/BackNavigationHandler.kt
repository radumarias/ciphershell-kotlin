package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

actual class BackNavigationHandler {
    @Composable
    actual fun OnBackPressed(callback: () -> Unit) {
        BackHandler(enabled = true, onBack = callback)
    }
}

actual fun provideBackNavigationHandler(): BackNavigationHandler {
    return BackNavigationHandler()
}