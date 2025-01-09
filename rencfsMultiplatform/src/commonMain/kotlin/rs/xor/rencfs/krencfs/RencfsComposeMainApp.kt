package rs.xor.rencfs.krencfs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.navigation.RencfsNavigation
import rs.xor.rencfs.krencfs.screen.SplashScreen

@Composable
fun RencfsComposeMainApp(deviceType: DisplayType) {
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
        RencfsNavigation(deviceType, count <= 0)
    }
}

