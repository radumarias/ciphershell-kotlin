package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.IconButton
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.wizzard_navigation_btn_next_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun NextButton(
    onClick: () -> Unit,
    isSaving: Boolean,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        enabled = !isSaving && isEnabled,
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (!isSaving && isEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                },
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription =
                stringResource(Res.string.wizzard_navigation_btn_next_content_description),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
