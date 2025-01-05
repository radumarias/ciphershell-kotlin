package rs.xor.rencfs.krencfs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


enum class RencfsNavigationRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val isTopLevel: Boolean = false,
) {
    VaultList("vaults", "Vaults", Icons.Filled.Folder, true),
    VaultCreate("vault", "Add Vault", Icons.Filled.Add),
    VaultView("vault/{vaultId}", "Vault Details", Icons.Filled.Folder),
    VaultEdit("vault/{vaultId}/edit", "Edit Vault", Icons.Filled.Edit),
    VaultDelete("vault/{vaultId}/delete", "Edit Vault", Icons.Filled.Remove),
    Settings("settings", "Settings", Icons.Filled.Settings, true),
    About("about", "About", Icons.Filled.Info, true);

    companion object {
        fun vaultDetailRoute(vaultId: String) = "vault/$vaultId"
        fun vaultEditRoute(vaultId: String) = "vault/$vaultId/edit"
    }
}
