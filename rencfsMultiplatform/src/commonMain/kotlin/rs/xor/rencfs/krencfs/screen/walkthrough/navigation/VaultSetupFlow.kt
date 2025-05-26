package rs.xor.rencfs.krencfs.screen.walkthrough.navigation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.core.context.GlobalContext.get
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.screen.walkthrough.ConfirmationScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.ExpertSettingsScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.FolderLocationScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.FolderNameScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.PasswordScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.RecoveryCodeScreen
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_CONFIRMATION
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_EXPERT_SETTINGS
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_FOLDER_LOCATION
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_FOLDER_NAME
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_PASSWORD
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_RECOVERY_CODE
import rs.xor.rencfs.krencfs.ui.state.ErrorState
import rs.xor.rencfs.krencfs.ui.state.LoadingState
import rs.xor.rencfs.krencfs.ui.state.UiState

@Composable
fun VaultSetupFlow(
    vaultId: String? = null,
    isEditMode: Boolean = false,
    isDesktop: Boolean,
    onViewDashboard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by remember { mutableStateOf(STEP_FOLDER_NAME) }
    var uiState by remember { mutableStateOf<UiState<VaultModel>>(UiState.Loading) }
    val isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val vaultRepository = remember { get().get<VaultRepository>() }

    LaunchedEffect(vaultId, isEditMode) {
        uiState = UiState.Loading
        try {
            if (isEditMode && vaultId != null) {
                val vault = vaultRepository.getVault(vaultId.toLong())
                uiState = if (vault != null) {
                    UiState.Success(vault)
                } else {
                    UiState.Error("Vault not found")
                }
            } else {
                val newVaultId = vaultRepository.addVault(
                    uri = null,
                    configureAdvancedSettings = 0L,
                    encryptionAlgorithm = null,
                    keySize = null,
                    recoveryCode = null,
                    isLocked = 1L,
                )
                val newVault = vaultRepository.getVault(newVaultId.toLong())!!
                uiState = UiState.Success(newVault)
            }
        } catch (e: Exception) {
            uiState = UiState.Error("Failed to load vault: ${e.message}")
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> LoadingState()
        is UiState.Error -> ErrorState(state.message)
        is UiState.Success -> {
            val vault = state.data
            when (currentStep) {
                STEP_FOLDER_NAME -> FolderNameScreen(
                    vault = vault,
                    onNext = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_FOLDER_LOCATION
                    },
                    isSaving = isSaving,
                    isDesktop = isDesktop,
                    modifier = modifier,
                )

                STEP_FOLDER_LOCATION -> FolderLocationScreen(
                    vault = vault,
                    onNext = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_EXPERT_SETTINGS
                    },
                    onBack = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_FOLDER_NAME
                    },
                    isSaving = isSaving,
                    isDesktop = isDesktop,
                    modifier = modifier,
                )

                STEP_EXPERT_SETTINGS -> ExpertSettingsScreen(
                    vault = vault,
                    onNext = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_PASSWORD
                    },
                    onBack = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_FOLDER_LOCATION
                    },
                    isSaving = isSaving,
                    isDesktop = isDesktop,
                    modifier = modifier,
                )

                STEP_PASSWORD -> PasswordScreen(
                    vault = vault,
                    onNext = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_RECOVERY_CODE
                    },
                    onBack = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_EXPERT_SETTINGS
                    },
                    isSaving = isSaving,
                    isDesktop = isDesktop,
                    modifier = modifier,
                )

                STEP_RECOVERY_CODE -> RecoveryCodeScreen(
                    vault = vault,
                    onNext = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_CONFIRMATION
                    },
                    onBack = { updatedVault ->
                        uiState = UiState.Success(updatedVault)
                        currentStep = STEP_PASSWORD
                    },
                    isDesktop = isDesktop,
                    modifier = modifier,
                )

                STEP_CONFIRMATION -> ConfirmationScreen(
                    vault = vault,
                    onBack = { _ ->
                        onViewDashboard()
                    },
                    onViewDashboard = onViewDashboard,
                    isDesktop = isDesktop,
                )
            }
        }
    }

    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            },
        )
    }
}
