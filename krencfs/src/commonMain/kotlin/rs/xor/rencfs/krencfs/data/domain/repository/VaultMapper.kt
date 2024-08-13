package rs.xor.rencfs.krencfs.data.domain.repository

import rs.xor.rencfs.krencfs.Vaults
import rs.xor.rencfs.krencfs.data.domain.model.VaultDataModel

fun Vaults.toVaultDataModel(): VaultDataModel {
    return VaultDataModel(
//        id = this.id.toString(),
        name = this.name,
        mountPoint = this.mount,
        dataDir = this.path
    )
}

fun VaultDataModel.toVaults(): Vaults {
    return Vaults(
        id = 0L,
        name = this.name,
        path = this.dataDir,
        mount = this.mountPoint
    )
}
