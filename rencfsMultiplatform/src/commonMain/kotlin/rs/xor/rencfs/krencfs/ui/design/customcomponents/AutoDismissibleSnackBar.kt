package rs.xor.rencfs.krencfs.ui.design.customcomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AutoDismissibleSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    autoDismissDelayMillis: Long = 3000,
    onDismiss: (() -> Unit)? = null,
) {
    var snackBarVisibleState by remember { mutableStateOf(true) }
    fun dismiss() {
        snackBarVisibleState = false
        onDismiss?.invoke()
    }
    LaunchedEffect(message) {
        delay(autoDismissDelayMillis)
        dismiss()
    }
    AnimatedVisibility(
        visible = snackBarVisibleState,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    )
    {
        Snackbar(
            modifier = modifier,
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
