package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import androidx.compose.runtime.Composable

expect class FolderPicker {
    @Composable
    fun LaunchFolderPicker(onResult: (uri: String?, displayName: String?) -> Unit)
}

expect fun provideFolderPicker(): FolderPicker
