package rs.xor.rencfs.krencfs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.ui.state.ErrorState
import rs.xor.rencfs.krencfs.ui.state.LoadingState
import rs.xor.rencfs.krencfs.ui.state.UiState

// VaultEditor.kt
@Composable
fun VaultEditor(
    modifier: Modifier = Modifier,
    vaultId: String? = null,
    createVault: Boolean = false,
    onSave: () -> Unit
) {
    var uiState by remember { mutableStateOf<UiState<VaultModel>>(UiState.Loading) }
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(vaultId) {
        uiState = UiState.Loading
        try {
            val repo = SQLDelightDB.getVaultRepositoryAsync()
            vaultId?.let {
                val vault = repo.getVault(it.toLong())
                uiState = if (vault != null) {
                    UiState.Success(vault)
                } else {
                    UiState.Error("Vault not found")
                }
            } ?: run {
                uiState = if (createVault) {
                    // TODO: Improve this, introduce drafting
                    val newVault = repo.getVault(repo.addVault().toLong())!!
                    UiState.Success(newVault)
                }
                else
                {
                    UiState.Error("Invalid vault ID")
                }
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
    var passwordVisible by remember { mutableStateOf(false) }

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

        // New Password Field
        OutlinedTextField(
            value = editedVault.password ?: "",
            onValueChange = {
                editedVault = editedVault.copy(password = it)
                hasChanges = true
            },
            label = { Text("Password") },
            enabled = !isSaving,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
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
