package rs.xor.rencfs.krencfs.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import kotlinx.coroutines.*
import rs.xor.rencfs.krencfs.KrenkfsDB
import rs.xor.rencfs.krencfs.VaultsQueries
import rs.xor.rencfs.krencfs.data.domain.repository.VaultLocalDataSource
import rs.xor.rencfs.krencfs.data.domain.repository.VaultRepository
import rs.xor.rencfs.krencfs.data.domain.repository.VaultRepositoryImpl

expect suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
    databaseFileName: String = "${databaseName}.sqlite.db",
): SqlDriver

object Database {

    @OptIn(DelicateCoroutinesApi::class)
    private val vaultsRepository: Deferred<VaultRepository> by lazy {
        GlobalScope.async(Dispatchers.IO) {
            val driver = provideSQLDriver(KrenkfsDB.Schema, "Vaults")
            val vaults = VaultsQueries(driver)
            val localVaultsSource = VaultLocalDataSource(vaults)
            VaultRepositoryImpl(localVaultsSource)
        }
    }

    suspend fun getVaultRepository(): VaultRepository {
        return vaultsRepository.await()
    }
    
}
