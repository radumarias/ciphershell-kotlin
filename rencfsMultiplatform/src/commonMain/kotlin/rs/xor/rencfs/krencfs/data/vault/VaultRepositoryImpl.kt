package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.map
import rs.xor.rencfs.krencfs.Vault

class VaultRepositoryImpl(
    private val dao: VaultDAO,
) : VaultRepository {
    override fun observeVaults() = dao.observeVaults().map { vaultsList ->
        vaultsList.associateByTo(LinkedHashMap(), { it.id.toString() }, { it })
    }

    override fun getVaultsPaged(
        limit: Long,
        offset: Long,
    ) = dao.getVaultsPaged(limit, offset).map { vaultsList ->
        vaultsList.associateByTo(LinkedHashMap(), { it.id!!.toLong() }, { it })
    }

    override fun getVault(id: Long): VaultModel? = dao.getVaultById(id)

    override suspend fun addVault(
        name: String,
        mountPoint: String,
        dataDir: String,
        uri: String?,
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
        isLocked: Long,
    ) = dao.insertVaultAndGetId(
        name,
        mountPoint,
        dataDir,
        uri,
        configureAdvancedSettings,
        encryptionAlgorithm,
        keySize,
        recoveryCode,
        isLocked,
    ).toString()

    override suspend fun updateVault(vault: Vault) {
        println("VaultRepositoryImpl Update vault ID: ${vault.id}, Name: ${vault.name}, URI: ${vault.uri}, Path: ${vault.path}, IsLocked: ${vault.isLocked}")
        dao.updateVault(
            id = vault.id,
            name = vault.name,
            mountPoint = vault.mount,
            dataDir = vault.path,
            uri = vault.uri,
            configureAdvancedSettings = vault.configureAdvancedSettings,
            encryptionAlgorithm = vault.encryptionAlgorithm,
            keySize = vault.keySize,
            recoveryCode = vault.recoveryCode,
            isLocked = vault.isLocked
        )
    }

    override suspend fun deleteVault(id: String) = id.toLongOrNull()?.let { dao.deleteVault(it) } ?: Unit

    override fun count() = dao.count()
}
