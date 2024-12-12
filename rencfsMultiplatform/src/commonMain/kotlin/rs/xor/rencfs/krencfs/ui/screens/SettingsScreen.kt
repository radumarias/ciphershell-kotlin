package rs.xor.rencfs.krencfs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var autoMount by remember { mutableStateOf(false) }
        var showNotifications by remember { mutableStateOf(true) }

        Text(
            text = "General Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingItem(
                    title = "Auto-mount Vaults",
                    subtitle = "Automatically mount vaults on startup",
                    checked = autoMount,
                    onCheckedChange = { autoMount = it }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SettingItem(
                    title = "Show Notifications",
                    subtitle = "Display notifications for mount/unmount events",
                    checked = showNotifications,
                    onCheckedChange = { showNotifications = it }
                )
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
