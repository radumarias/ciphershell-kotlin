package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_code_copy
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_code_description
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_code_title
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_print
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_save
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_saving
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_recovery_warning
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.get
import rs.xor.rencfs.krencfs.data.sqldelight.toVault
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_RECOVERY_CODE
import rs.xor.rencfs.krencfs.screen.walkthrough.utils.printRecoveryCode
import rs.xor.rencfs.krencfs.screen.walkthrough.utils.provideRecoveryCodeSaver
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingSmall

@Composable
fun RecoveryCodeScreen(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    isDesktop: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val recoveryCode = remember {
        vault.recoveryCode ?: generateRecoveryCode()
    }
    val recoveryCodeSaver = provideRecoveryCodeSaver()
    val editedVault by remember { mutableStateOf(vault.copy(recoveryCode = recoveryCode)) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val vaultRepository = remember { get().get<VaultRepository>() }
    val scope = rememberCoroutineScope()

    val clipboardManager = LocalClipboardManager.current

    WizardScreen(
        currentStep = STEP_RECOVERY_CODE,
        isSaving = isSaving,
        vault = editedVault,
        onNext = {
            saveVaultAndProceed(
                scope = scope,
                vaultRepository = vaultRepository,
                editedVault = editedVault,
                onNext = onNext,
                setIsSaving = { isSaving = it },
                setErrorMessage = { errorMessage = it },
            )
        },
        onBack = onBack,
        showBackButton = isDesktop,
        isNextEnabled = true,
        isDesktop = isDesktop,
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(horizontal = paddingNormal),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.wizzard_step_recovery_code_description) + "${editedVault.name}”.",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = paddingNormal),
            )

            OutlinedTextField(
                value = editedVault.recoveryCode ?: "",
                onValueChange = { /* Read-only */ },
                label = { Text(stringResource(Res.string.wizzard_step_recovery_code_title)) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(editedVault.recoveryCode ?: ""))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.wizzard_step_recovery_code_copy),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(paddingNormal))

            Text(
                text = stringResource(Res.string.wizzard_step_recovery_warning),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = paddingNormal),
            )

            errorMessage?.let { message ->
                Spacer(Modifier.height(paddingSmall))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = paddingNormal),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedButton(
                    onClick = {
                        try {
                            printRecoveryCode(
                                recoveryCode = editedVault.recoveryCode ?: "",
                                folderName = editedVault.name,
                            )
                        } catch (e: Exception) {
                            print("Failed to print: ${e.message}")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = paddingSmall),
                ) {
                    Text(stringResource(Res.string.wizzard_step_recovery_print))
                }

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                isSaving = true
                                errorMessage = null

                                editedVault.recoveryCode?.let { code ->
                                    val result = recoveryCodeSaver.saveRecoveryCode(
                                        recoveryCode = code,
                                        fileName = "recovery_code_${editedVault.name}.txt",
                                    )
                                    if (!result.isSuccess) {
                                        errorMessage =
                                            "Failed to save recovery code: ${result.exceptionOrNull()?.message}"
                                        return@launch
                                    }
                                }
                                saveVaultAndProceed(
                                    scope = scope,
                                    vaultRepository = vaultRepository,
                                    editedVault = editedVault,
                                    onNext = onNext,
                                    setIsSaving = { isSaving = it },
                                    setErrorMessage = { errorMessage = it },
                                )
                            } catch (e: Exception) {
                                errorMessage = "Failed to save vault: ${e.message}"
                            } finally {
                                isSaving = false
                            }
                        }
                    },
                    enabled = !isSaving,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = paddingSmall),
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(paddingNormal),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(paddingSmall))
                        Text(stringResource(Res.string.wizzard_step_recovery_saving))
                    } else {
                        Text(stringResource(Res.string.wizzard_step_recovery_save))
                    }
                }
            }
        }
    }
}

fun saveVaultAndProceed(
    scope: CoroutineScope,
    vaultRepository: VaultRepository,
    editedVault: VaultModel,
    onNext: (VaultModel) -> Unit,
    setIsSaving: (Boolean) -> Unit,
    setErrorMessage: (String?) -> Unit,
) {
    scope.launch {
        try {
            setIsSaving(true)
            setErrorMessage(null)
            vaultRepository.updateVault(editedVault.toVault())
            onNext(editedVault)
        } catch (e: Exception) {
            setErrorMessage("Failed to save vault: ${e.message}")
        } finally {
            setIsSaving(false)
        }
    }
}

// Generates recovery code with format: XXXX-XXXX-XXXX-XXXX
fun generateRecoveryCode(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..16)
        .map { chars.random() }
        .joinToString("")
        .chunked(4)
        .joinToString("-")
}
