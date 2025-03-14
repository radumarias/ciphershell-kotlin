package rs.xor.rencfs.krencfs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.core.bundle.Bundle
import kotlinx.serialization.Serializable
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.about_title
import krencfs.rencfsmultiplatform.generated.resources.settings_title
import krencfs.rencfsmultiplatform.generated.resources.vault_create_title
import krencfs.rencfsmultiplatform.generated.resources.vault_edit_title
import krencfs.rencfsmultiplatform.generated.resources.vault_list_title
import krencfs.rencfsmultiplatform.generated.resources.vault_view_title
import org.jetbrains.compose.resources.stringResource

@Serializable
sealed class RencfsRoute(
    val route: String,
    val isTopLevel: Boolean = false,
) {
    @Serializable
    data object VaultList : RencfsRoute(VAULT_LIST_ROUTE, isTopLevel = true)

    @Serializable
    data object VaultCreate : RencfsRoute(VAULT_CREATE_ROUTE)

    @Serializable
    data class VaultView(
        val vaultId: String,
    ) : RencfsRoute(routeWithArgs(vaultId)) {
        companion object {
            const val BASE_ROUTE = "$VAULT_VIEW_ROUTE/{$VAULT_PARAM_ID}"

            fun routeWithArgs(vaultId: String) = "$VAULT_VIEW_ROUTE/$vaultId"
        }
    }

    @Serializable
    data class VaultEdit(
        val vaultId: String,
    ) : RencfsRoute(routeWithArgs(vaultId)) {
        companion object {
            const val BASE_ROUTE = "$VAULT_EDIT_ROUTE/{$VAULT_PARAM_ID}"

            fun routeWithArgs(vaultId: String) = "$VAULT_EDIT_ROUTE/$vaultId"
        }
    }

    @Serializable
    data object Settings : RencfsRoute(SETTINGS_ROUTE, isTopLevel = true)

    @Serializable
    data object About : RencfsRoute(ABOUT_ROUTE, isTopLevel = true)

    companion object {
        fun fromRoute(
            route: String?,
            arguments: Bundle?,
        ) = when (route) {
            VAULT_LIST_ROUTE -> VaultList
            VAULT_CREATE_ROUTE -> VaultCreate
            VaultView.BASE_ROUTE -> VaultView(arguments.requireParam(VAULT_PARAM_ID))
            VaultEdit.BASE_ROUTE -> VaultEdit(arguments.requireParam(VAULT_PARAM_ID))
            SETTINGS_ROUTE -> Settings
            ABOUT_ROUTE -> About
            else -> null
        }

        // Vaults
        const val VAULT_PARAM_ID = "vaultId"
        const val VAULT_LIST_ROUTE = "vault_list"
        const val VAULT_CREATE_ROUTE = "vault_create"
        const val VAULT_VIEW_ROUTE = "vault_view"
        const val VAULT_EDIT_ROUTE = "vault_edit"

        // Settings
        const val SETTINGS_ROUTE = "settings"

        // About
        const val ABOUT_ROUTE = "about"
    }
}

fun Bundle?.requireParam(key: String) = this?.getString(key) ?: throw IllegalArgumentException("Missing $key")

@Composable
fun RencfsRoute.mapToTitle() = stringResource(
    when (this) {
        RencfsRoute.VaultList -> Res.string.vault_list_title
        RencfsRoute.VaultCreate -> Res.string.vault_create_title
        is RencfsRoute.VaultView -> Res.string.vault_view_title
        is RencfsRoute.VaultEdit -> Res.string.vault_edit_title
        RencfsRoute.Settings -> Res.string.settings_title
        RencfsRoute.About -> Res.string.about_title
        else -> throw IllegalArgumentException("Route $this is not supported")
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
    else -> throw IllegalArgumentException("Route $this is not supported")
}
