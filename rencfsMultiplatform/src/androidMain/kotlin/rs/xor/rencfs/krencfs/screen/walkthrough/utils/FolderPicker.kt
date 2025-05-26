package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri

actual class FolderPicker {
    @Composable
    actual fun LaunchFolderPicker(onResult: (uri: String?, displayName: String?) -> Unit) {
        var shouldLaunch by remember { mutableStateOf(true) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val uriString = uri.toString()
                    val displayName =
                        uri.lastPathSegment?.substringAfterLast(":") ?: "Unknown Folder"
                    onResult(uriString, displayName)
                } ?: onResult(null, null)
            } else {
                onResult(null, null)
            }
            shouldLaunch = false
        }

        if (shouldLaunch) {
            LaunchedEffect(Unit) {
                launcher.launch(
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        putExtra(
                            "android.provider.extra.INITIAL_URI",
                            "content://com.android.externalstorage.documents/tree/primary%3ADownload".toUri(),
                        )
                    },
                )
            }
        }
    }
}

actual fun provideFolderPicker(): FolderPicker = FolderPicker()
