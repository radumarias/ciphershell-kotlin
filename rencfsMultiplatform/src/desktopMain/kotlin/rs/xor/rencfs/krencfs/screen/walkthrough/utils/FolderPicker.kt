package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher

actual class FolderPicker {
    @Composable
    actual fun LaunchFolderPicker(onResult: (uri: String?, displayName: String?) -> Unit) {
        val launcher = rememberDirectoryPickerLauncher { directory ->
            if (directory != null) {
                val path = directory.path
                val displayName = directory.file.name
                onResult(path, displayName)
            } else {
                onResult(null, null)
            }
        }
        launcher.launch()
    }
}

actual fun provideFolderPicker(): FolderPicker = FolderPicker()
