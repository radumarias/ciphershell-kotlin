package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_confirmation
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_confirmation_text
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_confirmation_view_dashboard
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.WizardSteps.STEP_CONFIRMATION
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    vault: VaultModel,
    onViewDashboard: () -> Unit,
    onUnlockFolder: () -> Unit,
    onBack: (VaultModel) -> Unit,
    isDesktop: Boolean = true,
    modifier: Modifier = Modifier
) {
    WizardScreen(
        currentStep = STEP_CONFIRMATION,
        isSaving = false,
        vault = vault,
        onNext = { },
        onBack = onBack,
        onViewDashboard = onViewDashboard,
        showBackButton = false,
        isNextEnabled = false,
        hideNextButton = true,
        isDesktop = isDesktop,
        modifier = modifier
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxWidth()
                .padding(horizontal = paddingNormal),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = stringResource(Res.string.wizzard_step_confirmation),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(100.dp)
                )
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = stringResource(Res.string.wizzard_step_confirmation_text),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = paddingSmall)
            )
//TODO handle unlock folder
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
                OutlinedButton(
                    onClick = onViewDashboard,
                    modifier = Modifier
                        .padding(start = paddingSmall)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(Res.string.wizzard_step_confirmation_view_dashboard))
                }

//                TooltipBox(
//                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
//                    tooltip = {
//                        Surface(
//                            color = MaterialTheme.colorScheme.surfaceVariant,
//                            shape = MaterialTheme.shapes.small,
//                            modifier = Modifier.padding(4.dp)
//                        ) {
//                            Text(
//                                text = "Click here to unlock access to folder",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                modifier = Modifier.padding(paddingSmall)
//                            )
//                        }
//                    },
//                    state = rememberTooltipState(isPersistent = false)
//                ) {
//                    Button(
//                        onClick = onUnlockFolder,
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(start = paddingSmall)
//                    ) {
//                        Text(
//                            text = stringResource(Res.string.wizzard_step_confirmation_unlock_folder),
//                            maxLines = 1,
//                            style = MaterialTheme.typography.labelLarge
//                        )
//                    }
//                }
//            }
        }
    }
}