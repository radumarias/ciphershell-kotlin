package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.Vault

interface VaultDAO {

    fun observeVaults(): Flow<List<Vault>>
    fun getVaultsPaged(limit: Long, offset: Long): Flow<List<Vault>>
    fun getVaultById(id: Long): Vault?

    suspend fun insertVaultAndGetId(name: String, mountPoint: String, dataDir: String): Long

    suspend fun updateVault(id: Long, name: String, mountPoint: String, dataDir: String)
    suspend fun deleteVault(id: Long)

}
