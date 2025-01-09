package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.button_label_mount_vault
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.ui.state.ErrorState
import rs.xor.rencfs.krencfs.ui.state.LoadingState
import rs.xor.rencfs.krencfs.ui.state.UiState

@Composable
fun VaultViewer(
    vaultId: String?,
    modifier: Modifier = Modifier,
) {
    var uiState by remember { mutableStateOf<UiState<VaultModel>>(UiState.Loading) }

    LaunchedEffect(vaultId) {
        uiState = UiState.Loading
        try {
            vaultId?.let {
                val vault = SQLDelightDB.getVaultRepositoryAsync().getVault(it.toLong())
                uiState = if (vault != null) {
                    UiState.Success(vault)
                } else {
                    UiState.Error("Vault not found")
                }
            } ?: run {
                uiState = UiState.Error("Invalid vault ID")
            }
        } catch (e: Exception) {
            uiState = UiState.Error("Failed to load vault: ${e.message}")
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> LoadingState()
        is UiState.Success -> VaultContent(state.data, modifier)
        is UiState.Error -> ErrorState(state.message)
    }
}

@Composable
private fun VaultContent(
    vault: VaultModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ListItem(
                    headlineContent = { Text(vault.name) },
                    supportingContent = { Text("Name") }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(vault.mountPoint) },
                    supportingContent = { Text("Mount Point") }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(vault.dataDir) },
                    supportingContent = { Text("Data Directory") }
                )
            }
        }
        Button(
            onClick = { System.out.println("Mount: Cluck!") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(Res.string.button_label_mount_vault))
        }
    }
}
