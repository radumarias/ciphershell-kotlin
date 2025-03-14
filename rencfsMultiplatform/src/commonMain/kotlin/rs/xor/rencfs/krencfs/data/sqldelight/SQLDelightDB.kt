package rs.xor.rencfs.krencfs.data.sqldelight

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import rs.xor.rencfs.krencfs.KrenkfsDB
import rs.xor.rencfs.krencfs.VaultsQueries
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.data.vault.VaultRepositoryImpl

object SQLDelightDB {

    @OptIn(DelicateCoroutinesApi::class)
    private val vaultsRepository: Deferred<VaultRepository> by lazy {
        GlobalScope.async(Dispatchers.IO) {
            val driver = provideSQLDriver(KrenkfsDB.Schema, "Vaults")
            val vaults = VaultsQueries(driver)
            val localVaultsSource = SQLDelightVaultDAO(vaults)
            VaultRepositoryImpl(localVaultsSource)
        }
    }

//    fun getVaultRepository(): VaultRepository = runBlocking { vaultsRepository.await() }

    suspend fun getVaultRepositoryAsync(): VaultRepository = vaultsRepository.await()
}
