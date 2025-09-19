package rs.xor.rencfs.krencfs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.about_title
import krencfs.rencfsmultiplatform.generated.resources.add_new_folder_title
import krencfs.rencfsmultiplatform.generated.resources.encrypted_folders_title
import krencfs.rencfsmultiplatform.generated.resources.settings_title
import krencfs.rencfsmultiplatform.generated.resources.vault_edit_title
import krencfs.rencfsmultiplatform.generated.resources.vault_view_title
import org.jetbrains.compose.resources.stringResource

@Immutable
@Serializable
sealed class RencfsRoute(
    val isTopLevel: Boolean = false,
) {
    @Serializable
    data object VaultList : RencfsRoute(isTopLevel = true)

    @Serializable
    data object VaultCreate : RencfsRoute()

    @Serializable
    data class VaultView(
        val vaultId: String,
    ) : RencfsRoute()

    @Serializable
    data class VaultEdit(
        val vaultId: String,
    ) : RencfsRoute()

    @Serializable
    data object Settings : RencfsRoute(isTopLevel = true)

    @Serializable
    data object About : RencfsRoute(isTopLevel = true)

    companion object {
        // These constants are kept for backwards compatibility if needed elsewhere
        // but are no longer used in navigation with type-safe routing
    }
}


@Composable
fun RencfsRoute.mapToTitle() = stringResource(
    when (this) {
        RencfsRoute.VaultList -> Res.string.encrypted_folders_title
        RencfsRoute.VaultCreate -> Res.string.add_new_folder_title
        is RencfsRoute.VaultView -> Res.string.vault_view_title
        is RencfsRoute.VaultEdit -> Res.string.vault_edit_title
        RencfsRoute.Settings -> Res.string.settings_title
        RencfsRoute.About -> Res.string.about_title
    },
)

@Composable
fun RencfsRoute.mapToIcon() = when (this) {
    RencfsRoute.VaultList -> Icons.Filled.Home
    RencfsRoute.VaultCreate -> Icons.Filled.Add
    is RencfsRoute.VaultView -> Icons.Filled.Visibility
    is RencfsRoute.VaultEdit -> Icons.Filled.Edit
    RencfsRoute.Settings -> Icons.Filled.Settings
    RencfsRoute.About -> Icons.Filled.Info
}
