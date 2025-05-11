package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_folder_name_description
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_folder_name_title
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.WizardSteps.STEP_FOLDER_NAME
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal

@Composable
fun FolderNameScreen(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    isSaving: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    var editedVault by remember { mutableStateOf(vault) }

    WizardScreen(
        currentStep = STEP_FOLDER_NAME,
        isSaving = isSaving,
        vault = editedVault,
        onNext = onNext,
        onBack = {},
        isNextEnabled = editedVault.name.isNotBlank(),
        showBackButton = false,
        hideNextButton = false,
        isDesktop = isDesktop,
        modifier = modifier
    ) { contentModifier ->
        Column(
            modifier = contentModifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = editedVault.name,
                onValueChange = { newName ->
                    editedVault = editedVault.copy(name = newName)
                },
                label = { Text(stringResource(Res.string.wizzard_step_folder_name_title)) },
                enabled = !isSaving,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(paddingNormal))

            Text(
                text = stringResource(Res.string.wizzard_step_folder_name_description),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
