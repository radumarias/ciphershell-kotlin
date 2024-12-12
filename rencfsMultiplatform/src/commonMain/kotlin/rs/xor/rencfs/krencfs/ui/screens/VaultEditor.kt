package rs.xor.rencfs.krencfs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.ui.ErrorState
import rs.xor.rencfs.krencfs.ui.LoadingState
import rs.xor.rencfs.krencfs.ui.UiState

// VaultEditor.kt
@Composable
fun VaultEditor(
    vaultId: String?,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    var uiState by remember { mutableStateOf<UiState<VaultModel>>(UiState.Loading) }
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        is UiState.Success -> {
            VaultEditorContent(
                vault = state.data,
                onSave = { updatedVault ->
                    scope.launch {
                        try {
                            isSaving = true
                            errorMessage = null
                            vaultId?.let {
                                SQLDelightDB.getVaultRepositoryAsync().updateVault(
                                    it,
                                    updatedVault.name,
                                    updatedVault.dataDir,
                                    updatedVault.mountPoint
                                )
                                onSave()
                            }
                        } catch (e: Exception) {
                            errorMessage = "Failed to save changes: ${e.message}"
                        } finally {
                            isSaving = false
                        }
                    }
                },
                isSaving = isSaving,
                modifier = modifier
            )

            // Show error snackbar if there's an error
            errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("DISMISS")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
        is UiState.Error -> ErrorState(state.message)
    }
}

@Composable
private fun VaultEditorContent(
    vault: VaultModel,
    onSave: (VaultModel) -> Unit,
    isSaving: Boolean,
    modifier: Modifier = Modifier
) {
    var editedVault by remember { mutableStateOf(vault) }
    var hasChanges by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = editedVault.name,
            onValueChange = {
                editedVault = editedVault.copy(name = it)
                hasChanges = true
            },
            label = { Text("Name") },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = editedVault.mountPoint,
            onValueChange = {
                editedVault = editedVault.copy(mountPoint = it)
                hasChanges = true
            },
            label = { Text("Mount Point") },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        DataDirField(
            value = editedVault.dataDir,
            onValueChange = {
                editedVault = editedVault.copy(dataDir = it)
                hasChanges = true
            },
            enabled = !isSaving
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSave(editedVault) },
            modifier = Modifier.align(Alignment.End),
            enabled = hasChanges && !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(if (isSaving) "Saving..." else "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DataDirField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Data Directory") },
            enabled = enabled,
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        val launcher = rememberDirectoryPickerLauncher(
            title = "Choose data folder",
            initialDirectory = value
        ) { directory ->
            directory?.path?.let(onValueChange)
        }

        IconButton(
            onClick = { launcher.launch() },
            enabled = enabled,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                Icons.Outlined.MoreHoriz,
                contentDescription = "Browse",
                tint = if (enabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}
