package rs.xor.rencfs.krencfs.data.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.Vaults
import rs.xor.rencfs.krencfs.VaultsQueries

class VaultLocalDataSource(
    private val vaultsQueries: VaultsQueries,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun observeVaults(): Flow<List<Vaults>> {
        return vaultsQueries
            .selectAll()
            .asFlow()
            .mapToList(ioDispatcher)
    }

    suspend fun insertVaultAndGetId(name: String, mountPoint: String, dataDir: String): Long {
        return vaultsQueries.transactionWithResult {
            vaultsQueries.insertVault(name, dataDir, mountPoint)
            vaultsQueries.selectLastInsertId().executeAsOne()
        }
    }

    suspend fun updateVault(id: Long, name: String, mountPoint: String, dataDir: String) {
        println("Update vault id: $id, name: $name, mountPoint: $mountPoint, dataDir: $dataDir")
        vaultsQueries.updateVault(name, dataDir, mountPoint, id)
    }

    suspend fun deleteVault(id: Long) {
        vaultsQueries.deleteVault(id)
    }

    fun getVaultsPaged(limit: Long, offset: Long): Flow<List<Vaults>> {
        return vaultsQueries.selectVaultsPaged(limit, offset).asFlow().mapToList(ioDispatcher)
    }
}

