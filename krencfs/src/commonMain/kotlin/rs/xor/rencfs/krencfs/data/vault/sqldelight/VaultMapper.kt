package rs.xor.rencfs.krencfs.data.vault.sqldelight

import rs.xor.rencfs.krencfs.Vaults
import rs.xor.rencfs.krencfs.data.vault.VaultModel

fun Vaults.toVaultDataModel(): VaultModel {
    return VaultModel(
        name = this.name,
        mountPoint = this.mount,
        dataDir = this.path
    )
}

fun VaultModel.toVaults(): Vaults {
    return Vaults(
        id = 0L,
        name = this.name,
        path = this.dataDir,
        mount = this.mountPoint,
    )
}
