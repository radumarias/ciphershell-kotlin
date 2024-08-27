package rs.xor.rencfs.krencfs.data.vault.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.Vault
import rs.xor.rencfs.krencfs.VaultsQueries
import rs.xor.rencfs.krencfs.data.vault.VaultDAO


class SqlDelightVaultDAO(
    private val vaultsQueries: VaultsQueries,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VaultDAO {

    override fun observeVaults(): Flow<List<Vault>> = vaultsQueries
        .selectAll()
        .asFlow()
        .mapToList(ioDispatcher)

    override suspend fun insertVaultAndGetId(name: String, mountPoint: String, dataDir: String) =
        vaultsQueries.transactionWithResult {
            vaultsQueries.insertVault(name, dataDir, mountPoint)
            vaultsQueries.selectLastInsertId().executeAsOne()
        }

    override suspend fun updateVault(id: Long, name: String, mountPoint: String, dataDir: String) =
        vaultsQueries.updateVault(name, dataDir, mountPoint, id)

    override suspend fun deleteVault(id: Long) = vaultsQueries.deleteVault(id)

    override fun getVaultsPaged(limit: Long, offset: Long) =
        vaultsQueries.selectVaultsPaged(limit, offset).asFlow().mapToList(ioDispatcher)

    override fun getVaultById(id: Long) = vaultsQueries.selectById(id).executeAsOneOrNull()
}

