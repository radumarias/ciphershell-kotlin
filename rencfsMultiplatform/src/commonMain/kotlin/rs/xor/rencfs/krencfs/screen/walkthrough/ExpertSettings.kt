package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_expert_settings_description
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_expert_settings_dropdown
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_expert_settings_encryption_al
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_expert_settings_radio_sel_no
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_expert_settings_radio_sel_yes
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.EncryptionConstants.ALGORITHM_AES_GCM
import rs.xor.rencfs.krencfs.screen.walkthrough.EncryptionConstants.ALGORITHM_CHACHA20_POLY1305
import rs.xor.rencfs.krencfs.screen.walkthrough.EncryptionConstants.KEY_SIZE_128_BIT
import rs.xor.rencfs.krencfs.screen.walkthrough.EncryptionConstants.KEY_SIZE_192_BIT
import rs.xor.rencfs.krencfs.screen.walkthrough.EncryptionConstants.KEY_SIZE_256_BIT
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_EXPERT_SETTINGS
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingExtraSmall
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingSmall

object EncryptionConstants {
    const val KEY_SIZE_128_BIT = "128-bit"
    const val KEY_SIZE_192_BIT = "192-bit"
    const val KEY_SIZE_256_BIT = "256-bit"
    const val ALGORITHM_CHACHA20_POLY1305 = "ChaCha20Poly1305"
    const val ALGORITHM_AES_GCM = "AES-GCM"
}

@Composable
fun ExpertSettingsScreen(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    isSaving: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
) {
    var editedVault by remember { mutableStateOf(vault) }
    var selectedAlgorithm by remember {
        mutableStateOf(editedVault.encryptionAlgorithm ?: ALGORITHM_CHACHA20_POLY1305)
    }
    var selectedKeySize by remember {
        mutableStateOf(
            editedVault.keySize
                ?: if (selectedAlgorithm == ALGORITHM_CHACHA20_POLY1305) {
                    KEY_SIZE_256_BIT
                } else {
                    KEY_SIZE_128_BIT
                },
        )
    }

    WizardScreen(
        currentStep = STEP_EXPERT_SETTINGS,
        isSaving = isSaving,
        vault = editedVault,
        onNext = {
            editedVault = editedVault.copy(
                encryptionAlgorithm = if (editedVault.configureAdvancedSettings) selectedAlgorithm else null,
                keySize = if (editedVault.configureAdvancedSettings) selectedKeySize else null,
            )
            onNext(editedVault)
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
                text = stringResource(Res.string.wizzard_step_expert_settings_description),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = paddingNormal),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                RadioButtonWithLabel(
                    label = stringResource(Res.string.wizzard_step_expert_settings_radio_sel_no),
                    selected = !editedVault.configureAdvancedSettings,
                    onClick = {
                        editedVault = editedVault.copy(configureAdvancedSettings = false)
                    },
                )

                RadioButtonWithLabel(
                    label = stringResource(Res.string.wizzard_step_expert_settings_radio_sel_yes),
                    selected = editedVault.configureAdvancedSettings,
                    onClick = {
                        editedVault = editedVault.copy(configureAdvancedSettings = true)
                    },
                )
            }

            if (editedVault.configureAdvancedSettings) {
                Spacer(Modifier.height(24.dp))

                DropdownMenuField(
                    label = stringResource(Res.string.wizzard_step_expert_settings_encryption_al),
                    options = listOf(
                        ALGORITHM_CHACHA20_POLY1305,
                        ALGORITHM_AES_GCM,
                    ),
                    selectedOption = selectedAlgorithm,
                    onOptionSelected = { algorithm ->
                        selectedAlgorithm = algorithm
                        selectedKeySize = if (algorithm == ALGORITHM_CHACHA20_POLY1305) {
                            KEY_SIZE_256_BIT
                        } else {
                            KEY_SIZE_128_BIT
                        }
                    },
                )

                Spacer(Modifier.height(paddingNormal))

                DropdownMenuField(
                    label = selectedAlgorithm,
                    options = if (selectedAlgorithm == ALGORITHM_CHACHA20_POLY1305) {
                        listOf(KEY_SIZE_256_BIT)
                    } else {
                        listOf(
                            KEY_SIZE_128_BIT,
                            KEY_SIZE_192_BIT,
                            KEY_SIZE_256_BIT,
                        )
                    },
                    selectedOption = selectedKeySize,
                    onOptionSelected = { keySize ->
                        selectedKeySize = keySize
                    },
                )
            }
        }
    }
}

@Composable
fun RadioButtonWithLabel(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Spacer(Modifier.width(paddingExtraSmall))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd),
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { /* Read-only */ },
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(Res.string.wizzard_step_expert_settings_dropdown),
                    )
                }
            },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
