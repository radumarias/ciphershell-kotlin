package rs.xor.rencfs.krencfs.data.vault.sqldelight

import rs.xor.rencfs.krencfs.Vault
import rs.xor.rencfs.krencfs.data.vault.VaultModel

fun Vault.toVaultDataModel(): VaultModel {
    return VaultModel(
        name = this.name,
        mountPoint = this.mount,
        dataDir = this.path
    )
}
