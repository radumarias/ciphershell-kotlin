package rs.xor.rencfs.krencfs.ui.design.customcomponents

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ClickableText(
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isClickable: Boolean = onClick != null, // Default to true if onClick is provided
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            textDecoration = if (isClickable) TextDecoration.Underline else null,
            color = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.then(
            if (isClickable && onClick != null) {
                Modifier.clickable { onClick() }
            } else {
                Modifier
            },
        ),
    )
}
