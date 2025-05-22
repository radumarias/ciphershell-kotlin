package rs.xor.rencfs.krencfs.screen.walkthrough.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import rs.xor.rencfs.krencfs.data.vault.VaultModel
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

@Composable
fun VaultSetupFlow(
    isDesktop: Boolean,
    onViewDashboard: () -> Unit,
) {
    var currentStep by remember { mutableStateOf(STEP_FOLDER_NAME) }
    var vault by remember {
        mutableStateOf(
            VaultModel(
                id = null,
                name = "",
                mountPoint = "",
                dataDir = "",
                password = null,
            ),
        )
    }

    when (currentStep) {
        STEP_FOLDER_NAME -> FolderNameScreen(
            vault = vault,
            onNext = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_FOLDER_LOCATION
            },
            isSaving = false,
            isDesktop = isDesktop,
        )

        STEP_FOLDER_LOCATION -> FolderLocationScreen(
            vault = vault,
            onNext = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_EXPERT_SETTINGS
            },
            onBack = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_FOLDER_NAME
            },
            isSaving = false,
            isDesktop = isDesktop,
        )

        STEP_EXPERT_SETTINGS -> ExpertSettingsScreen(
            vault = vault,
            onNext = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_PASSWORD
            },
            onBack = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_FOLDER_LOCATION
            },
            isSaving = false,
            isDesktop = isDesktop,
        )

        STEP_PASSWORD -> PasswordScreen(
            vault = vault,
            onNext = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_RECOVERY_CODE
            },
            onBack = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_EXPERT_SETTINGS
            },
            isSaving = false,
            isDesktop = isDesktop,
        )

        STEP_RECOVERY_CODE -> RecoveryCodeScreen(
            vault = vault,
            onNext = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_CONFIRMATION
            },
            onBack = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_PASSWORD
            },
            isDesktop = isDesktop,
        )

        STEP_CONFIRMATION -> ConfirmationScreen(
            vault = vault,
            onBack = { updatedVault ->
                vault = updatedVault
                currentStep = STEP_RECOVERY_CODE
            },
            onViewDashboard = onViewDashboard,
            isDesktop = isDesktop,
        )
    }
}
