package rs.xor.rencfs.krencfs.data.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import rs.xor.rencfs.krencfs.VaultsQueries
import rs.xor.rencfs.krencfs.data.vault.VaultDAO

class SQLDelightVaultDAO(
    private val vaultsQueries: VaultsQueries,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : VaultDAO {
    override fun observeVaults() = vaultsQueries
        .selectAll()
        .asFlow()
        .mapToList(ioDispatcher)
        .toVaultDataModelFlow()

    override fun getVaultsPaged(
        limit: Long,
        offset: Long,
    ) = vaultsQueries
        .selectVaultsPaged(limit, offset)
        .asFlow()
        .mapToList(ioDispatcher)
        .toVaultDataModelFlow()

    override fun getVaultById(id: Long) = vaultsQueries.selectById(id).executeAsOneOrNull()?.toVaultDataModel()

    override suspend fun insertVaultAndGetId(
        name: String,
        mountPoint: String,
        dataDir: String,
        uri: String?,
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
        isLocked: Long,
    ) = ioDispatcher.invoke {
        vaultsQueries.transactionWithResult {
            vaultsQueries.insertVault(
                name,
                dataDir,
                uri,
                mountPoint,
                configureAdvancedSettings,
                encryptionAlgorithm,
                keySize,
                recoveryCode,
                isLocked,
            )
            vaultsQueries.selectLastInsertId().executeAsOne()
        }
    }

    override suspend fun updateVault(
        id: Long,
        name: String,
        mountPoint: String,
        dataDir: String,
        uri: String?,
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
        isLocked: Long,
    ) = ioDispatcher.invoke {
        vaultsQueries.updateVault(
            name = name,
            path = dataDir,
            uri = uri,
            mount = mountPoint,
            configureAdvancedSettings = configureAdvancedSettings,
            encryptionAlgorithm = encryptionAlgorithm,
            keySize = keySize,
            recoveryCode = recoveryCode,
            isLocked = isLocked,
            id = id
        )
    }

    override suspend fun deleteVault(id: Long) = ioDispatcher.invoke { vaultsQueries.deleteVault(id) }

    override fun count() = vaultsQueries.count().executeAsOne()
}
