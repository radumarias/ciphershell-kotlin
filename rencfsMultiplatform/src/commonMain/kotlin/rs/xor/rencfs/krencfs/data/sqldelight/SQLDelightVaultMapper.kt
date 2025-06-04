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
    uri = this.uri,
    configureAdvancedSettings = this.configureAdvancedSettings == 1L,
    encryptionAlgorithm = this.encryptionAlgorithm,
    keySize = this.keySize,
    recoveryCode = this.recoveryCode,
    isLocked = this.isLocked == 1L,
)

fun VaultModel.toVault(): Vault = Vault(
    id = id?.toLongOrNull() ?: throw IllegalArgumentException("Invalid vault ID: $id"),
    name = name,
    path = dataDir,
    uri = uri,
    mount = mountPoint,
    configureAdvancedSettings = if (configureAdvancedSettings) 1L else 0L,
    encryptionAlgorithm = encryptionAlgorithm,
    keySize = keySize,
    recoveryCode = recoveryCode,
    isLocked = if (isLocked) 1L else 0L,
)

fun List<Vault>.toVaultDataModelList(): List<VaultModel> = this.map { it.toVaultDataModel() }

fun Flow<List<Vault>>.toVaultDataModelFlow(): Flow<List<VaultModel>> = this.map { it.toVaultDataModelList() }
