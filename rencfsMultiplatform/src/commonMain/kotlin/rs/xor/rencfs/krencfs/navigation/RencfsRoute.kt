package rs.xor.rencfs.krencfs.navigation

sealed class RencfsRoute(val route: String, val title: String, val isTopLevel: Boolean = false) {
    data object VaultList : RencfsRoute("vault_list", "Vaults", isTopLevel = true)
    data object VaultCreate : RencfsRoute("vault_create", "Create Vault")
    data class VaultView(val vaultId: String) : RencfsRoute("vault_view/{vaultId}", "Vault") {
        companion object {
            const val BASE_ROUTE = "vault_view/{vaultId}"
            fun routeWithArgs(vaultId: String) = "vault_view/$vaultId"
        }
    }
    data class VaultEdit(val vaultId: String) : RencfsRoute("vault_edit/{vaultId}", "Edit Vault") {
        companion object {
            const val BASE_ROUTE = "vault_edit/{vaultId}"
            fun routeWithArgs(vaultId: String) = "vault_edit/$vaultId"
        }
    }
    data object Settings : RencfsRoute("settings", "Settings", isTopLevel = true)
    data object About : RencfsRoute("about", "About", isTopLevel = true)

    companion object {
        const val VAULT_ID_PARAM = "vaultId"
        val entries: List<RencfsRoute> = listOf(VaultList, VaultCreate, Settings, About)
    }
}