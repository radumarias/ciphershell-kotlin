package rs.xor.rencfs.krencfs.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel

@Composable
fun VaultListScreen(
    onVaultSelected: (String) -> Unit
) {
    var vaults by remember { mutableStateOf<Map<String, VaultModel>>(emptyMap()) }

    LaunchedEffect(Unit) {
        SQLDelightDB.getVaultRepositoryAsync()
            .observeVaults()
            .collect { newVaults ->
                vaults = newVaults
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        VaultList(
            vaults = vaults,
            onVaultClick = onVaultSelected
        )
    }
}

@Composable
private fun VaultList(
    vaults: Map<String, VaultModel>,
    onVaultClick: (String) -> Unit
) {
    LazyColumn {
        items(vaults.entries.toList(), key = { it.key }) { entry ->
            VaultListItem(
                vault = entry.value,
                onClick = { onVaultClick(entry.key) }
            )
        }
    }
}

@Composable
private fun VaultListItem(
    vault: VaultModel,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = vault.name)
            Text(
                text = vault.mountPoint,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}