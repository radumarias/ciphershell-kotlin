package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.settings_auto_mount_vaults_subtitle
import krencfs.rencfsmultiplatform.generated.resources.settings_auto_mount_vaults_title
import krencfs.rencfsmultiplatform.generated.resources.settings_show_notifications_subtitle
import krencfs.rencfsmultiplatform.generated.resources.settings_show_notifications_title
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingExtraSmall
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingSmall

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingNormal),
    ) {
        var autoMount by remember { mutableStateOf(false) }
        var showNotifications by remember { mutableStateOf(true) }

        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(paddingNormal)) {
                SettingItem(
                    title = stringResource(
                        Res.string.settings_auto_mount_vaults_title,
                    ),
                    subtitle = stringResource(
                        Res.string.settings_auto_mount_vaults_subtitle,
                    ),
                    checked = autoMount,
                    onCheckedChange = { autoMount = it },
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = paddingSmall))

                SettingItem(
                    title = stringResource(
                        Res.string.settings_show_notifications_title,
                    ),
                    subtitle = stringResource(
                        Res.string.settings_show_notifications_subtitle,
                    ),
                    checked = showNotifications,
                    onCheckedChange = { showNotifications = it },
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
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingExtraSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
