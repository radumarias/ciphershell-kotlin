package rs.xor.rencfs.krencfs.ui.screen.walkthrough

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.screen.walkthrough.FolderLocationScreen
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme

@Preview
@Composable
fun FolderLocationScreenPreview() {
    RencfsMaterialDarkTheme {
        FolderLocationScreen(
            vault =VaultModel(
                id = null,
                name = "",
                mountPoint = "",
                dataDir = "",
                password = null
            ),
            onNext = {},
            onBack = {},
            isSaving = false,
            isDesktop = false,
        )
    }
}