package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_folder_location_description
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_folder_location_select_folder
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_folder_location_title
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_FOLDER_LOCATION
import rs.xor.rencfs.krencfs.screen.walkthrough.utils.provideFolderPicker
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal

@Composable
fun FolderLocationScreen(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    isSaving: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
) {
    var editedVault by remember { mutableStateOf(vault) }
    val folderPicker = remember { provideFolderPicker() }
    var launchFolderPicker by remember { mutableStateOf(false) }

    if (launchFolderPicker) {
        folderPicker.LaunchFolderPicker { uri, displayName ->
            if (!uri.isNullOrEmpty() && !displayName.isNullOrEmpty()) {
                editedVault = editedVault.copy(dataDir = displayName, uri = uri)
            }
            launchFolderPicker = false
        }
    }

    WizardScreen(
        currentStep = STEP_FOLDER_LOCATION,
        isSaving = isSaving,
        vault = editedVault,
        onNext = onNext,
        onBack = onBack,
        showBackButton = isDesktop,
        isNextEnabled = editedVault.dataDir.isNotBlank(),
        isDesktop = isDesktop,
        modifier = modifier,
    ) { contentModifier ->
        Column(
            modifier = contentModifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                value = editedVault.dataDir,
                onValueChange = { newDataDir ->
                    editedVault = editedVault.copy(dataDir = newDataDir)
                },
                label = { Text(stringResource(Res.string.wizzard_step_folder_location_title)) },
                enabled = !isSaving,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { launchFolderPicker = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = stringResource(Res.string.wizzard_step_folder_location_select_folder),
                            tint = if (!isSaving) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            },
                        )
                    }
                },
            )

            Spacer(Modifier.height(paddingNormal))

            Text(
                text = stringResource(Res.string.wizzard_step_folder_location_description),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
