package rs.xor.rencfs.krencfs.data.sqldelight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.xor.rencfs.krencfs.Vault
import rs.xor.rencfs.krencfs.data.vault.VaultModel

fun Vault.toVaultDataModel(): VaultModel = VaultModel(
    id = this.id.toString(),
    name = this.name,
    mountPoint = this.mount,
    dataDir = this.path,
)

fun List<Vault>.toVaultDataModelList(): List<VaultModel> = this.map { it.toVaultDataModel() }

fun Flow<List<Vault>>.toVaultDataModelFlow(): Flow<List<VaultModel>> = this.map { it.toVaultDataModelList() }
