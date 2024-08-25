package rs.xor.rencfs.krencfs.data.domain.model

import rs.xor.rencfs.krencfs.Vaults

data class VaultDataModel(
    val name: String,
    val mountPoint: String,
    val dataDir: String,
)

fun Vaults.toVaultDataModel(): VaultDataModel {
    return VaultDataModel(
        name = this.name,
        mountPoint = this.mount,
        dataDir = this.path,
    )
}
