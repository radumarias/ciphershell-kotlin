package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_confirm
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_confirm_hide
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_confirm_show
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_hide
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_not_match
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_show
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_strength
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_strength_medium
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_strength_strong
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_strength_week
import krencfs.rencfsmultiplatform.generated.resources.wizzard_step_password_title
import org.jetbrains.compose.resources.stringResource
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.domain.walkthrough.PasswordStrength
import rs.xor.rencfs.krencfs.domain.walkthrough.calculatePasswordStrength
import rs.xor.rencfs.krencfs.screen.walkthrough.navigation.WizardSteps.STEP_PASSWORD
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingSmall

@Composable
fun PasswordScreen(
    vault: VaultModel,
    onNext: (VaultModel) -> Unit,
    onBack: (VaultModel) -> Unit,
    isSaving: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
) {
    var editedVault by remember { mutableStateOf(vault) }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordStrength = calculatePasswordStrength(editedVault.password ?: "")
    val passwordsMatch = editedVault.password == confirmPassword
    val isPasswordValid = passwordStrength != PasswordStrength.WEAK &&
        editedVault.password != null &&
        editedVault.password!!.isNotBlank()

    WizardScreen(
        currentStep = STEP_PASSWORD,
        isSaving = isSaving,
        vault = editedVault,
        onNext = onNext,
        onBack = onBack,
        showBackButton = isDesktop,
        isNextEnabled = isPasswordValid && passwordsMatch,
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
            OutlinedTextField(
                value = editedVault.password ?: "",
                onValueChange = { newPassword ->
                    editedVault = editedVault.copy(password = newPassword)
                },
                label = { Text(stringResource(Res.string.wizzard_step_password_title)) },
                enabled = !isSaving,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) {
                                stringResource(Res.string.wizzard_step_password_hide)
                            } else {
                                stringResource(Res.string.wizzard_step_password_show)
                            },
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(paddingSmall))

            PasswordStrengthIndicator(strength = passwordStrength)

            Spacer(Modifier.height(paddingNormal))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(Res.string.wizzard_step_password_confirm)) },
                enabled = !isSaving,
                visualTransformation = if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (confirmPasswordVisible) {
                                stringResource(Res.string.wizzard_step_password_confirm_hide)
                            } else {
                                stringResource(Res.string.wizzard_step_password_confirm_show)
                            },
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (confirmPassword.isNotBlank() && !passwordsMatch) {
                Spacer(Modifier.height(paddingSmall))
                Text(
                    text = stringResource(Res.string.wizzard_step_password_not_match),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {
    val (text, color) = when (strength) {
        PasswordStrength.WEAK ->
            stringResource(Res.string.wizzard_step_password_strength_week) to MaterialTheme.colorScheme.error
        PasswordStrength.MEDIUM ->
            stringResource(Res.string.wizzard_step_password_strength_medium) to MaterialTheme.colorScheme.primary
        PasswordStrength.STRONG ->
            stringResource(Res.string.wizzard_step_password_strength_strong) to MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(Res.string.wizzard_step_password_strength)} $text",
            style = MaterialTheme.typography.bodySmall,
            color = color,
        )
    }
}
