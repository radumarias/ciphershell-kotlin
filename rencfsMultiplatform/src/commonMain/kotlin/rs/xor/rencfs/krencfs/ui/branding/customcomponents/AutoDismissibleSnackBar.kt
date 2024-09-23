package rs.xor.rencfs.krencfs.design.customcomponents

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import rs.xor.rencfs.krencfs.ui.branding.DesignSystem

@Composable
fun AutoDismissibleSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    autoDismissDelayMillis: Long = 3000,
    onDismiss: (() -> Unit)? = null,
) {
    var snackbarVisibleState by remember { mutableStateOf(true) }
    fun dismiss() {
        snackbarVisibleState = false
        onDismiss?.invoke()
    }
    LaunchedEffect(message) {
        delay(autoDismissDelayMillis)
        dismiss()
    }
    AnimatedVisibility(
        modifier = modifier,
        visible = snackbarVisibleState,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    )
    {
        Snackbar(
            modifier = modifier
                .wrapContentSize(),
            action = {
                Text(
                    modifier = Modifier
                        .clickable { dismiss() }
                        .padding(10.dp),
                    text = "X"
                )
            },
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = message
            )
        }
    }
}
