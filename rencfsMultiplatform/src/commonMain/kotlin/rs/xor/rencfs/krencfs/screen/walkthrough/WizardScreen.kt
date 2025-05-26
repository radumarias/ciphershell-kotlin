package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_navigation_btn_back_content_description
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.components.NextButton
import rs.xor.rencfs.krencfs.screen.walkthrough.components.StepIndicator
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.TOTAL_STEPS
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.provideBackNavigationHandler
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal

@Composable
fun WizardScreen(
    currentStep: Int,
    totalSteps: Int = TOTAL_STEPS,
    isSaving: Boolean,
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    onViewDashboard: () -> Unit = {},
    showBackButton: Boolean,
    isNextEnabled: Boolean,
    hideNextButton: Boolean = false,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    val backNavigationHandler = provideBackNavigationHandler()
    val backAction: () -> Unit = if (currentStep == totalSteps) {
        { onViewDashboard() }
    } else {
        { onBack(vault) }
    }
    backNavigationHandler.OnBackPressed {
        backAction()
    }

    if (isDesktop) {
        DesktopLayout(
            currentStep = currentStep,
            totalSteps = totalSteps,
            isSaving = isSaving,
            vault = vault,
            onNext = onNext,
            onBack = onBack,
            showBackButton = showBackButton,
            isNextEnabled = isNextEnabled,
            hideNextButton = hideNextButton,
            modifier = modifier,
            content = content,
        )
    } else {
        AndroidLayout(
            currentStep = currentStep,
            totalSteps = totalSteps,
            isSaving = isSaving,
            vault = vault,
            onNext = onNext,
            isNextEnabled = isNextEnabled,
            hideNextButton = hideNextButton,
            modifier = modifier,
            content = content,
        )
    }
}

@Composable
fun NavigationButtonsAndroid(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    isNextEnabled: Boolean,
    hideNextButton: Boolean,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingNormal, vertical = paddingNormal),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (hideNextButton) {
            Spacer(Modifier.size(48.dp))
        } else {
            NextButton(
                onClick = { onNext(vault) },
                isSaving = isSaving,
                isEnabled = isNextEnabled,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun DesktopLayout(
    currentStep: Int,
    totalSteps: Int,
    isSaving: Boolean,
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    showBackButton: Boolean,
    isNextEnabled: Boolean,
    hideNextButton: Boolean,
    modifier: Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (showBackButton) {
            IconButton(
                onClick = { onBack(vault) },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = paddingNormal)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription =
                    stringResource(Res.string.wizzard_navigation_btn_back_content_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            Spacer(Modifier.size(24.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.Center)
                .padding(horizontal = paddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content(Modifier)
        }

        if (!hideNextButton) {
            NextButton(
                onClick = { onNext(vault) },
                isSaving = isSaving,
                isEnabled = isNextEnabled,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = paddingNormal),
            )
        }

        StepIndicator(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingNormal),
        )
    }
}

@Composable
fun AndroidLayout(
    currentStep: Int,
    totalSteps: Int,
    isSaving: Boolean,
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    isNextEnabled: Boolean,
    hideNextButton: Boolean,
    modifier: Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            content(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingNormal),
            )
        }

        NavigationButtonsAndroid(
            vault = vault,
            onNext = onNext,
            isNextEnabled = isNextEnabled,
            hideNextButton = hideNextButton,
            isSaving = isSaving,
        )

        StepIndicator(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(vertical = paddingNormal),
        )
    }
}
