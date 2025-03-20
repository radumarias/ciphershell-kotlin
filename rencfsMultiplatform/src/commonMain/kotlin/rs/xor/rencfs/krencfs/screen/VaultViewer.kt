package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.core.context.GlobalContext.get
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.ui.state.ErrorState
import rs.xor.rencfs.krencfs.ui.state.LoadingState
import rs.xor.rencfs.krencfs.ui.state.UiState

@Composable
fun VaultViewer(
    vaultId: String?,
    modifier: Modifier = Modifier,
) {
    var uiState by remember { mutableStateOf<UiState<VaultModel>>(UiState.Loading) }
    val vaultRepository = remember { get().get<VaultRepository>() }

    LaunchedEffect(vaultId) {
        uiState = UiState.Loading
        try {
            vaultId?.let {
                val vault = vaultRepository.getVault(it.toLong())
                uiState = if (vault != null) {
                    UiState.Success(vault)
                } else {
                    UiState.Error("Vault not found $vaultId")
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
private fun VaultLockSwitch(
    isLocked: Boolean,
    onLockStateChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = isLocked,
            onCheckedChange = onLockStateChanged,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                contentDescription = if (isLocked) "Locked" else "Unlocked",
                tint = if (isLocked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
            Text(
                text = if (isLocked) "Locked" else "Unlocked",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun VaultContent(
    vault: VaultModel,
    modifier: Modifier = Modifier,
) {
    var isLocked by remember { mutableStateOf(true) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var pendingLockState by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }

    println(vault)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                ListItem(
                    headlineContent = { Text(vault.name) },
                    supportingContent = { Text("Name") },
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(vault.mountPoint) },
                    supportingContent = { Text("Mount Point") },
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(vault.dataDir) },
                    supportingContent = { Text("Data Directory") },
                )
            }
        }

        VaultLockSwitch(
            isLocked = isLocked,
            onLockStateChanged = { newState ->
                pendingLockState = newState
                showPasswordDialog = true
            },
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }

    if (showPasswordDialog) {
        PasswordDialog(
            onConfirm = { password ->
                isLocked = pendingLockState
                showPasswordDialog = false
                println("Lock state changed to: $pendingLockState with password: $password")
            },
            onDismiss = {
                showPasswordDialog = false
                pendingLockState = isLocked
            },
        )
    }
}

@Composable
private fun PasswordDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Password") },
        text = {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            },
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(password) },
                enabled = password.isNotEmpty(),
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
