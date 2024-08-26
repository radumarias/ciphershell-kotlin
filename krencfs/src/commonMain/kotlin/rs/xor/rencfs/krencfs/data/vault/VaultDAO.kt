package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.Vaults

interface VaultDAO {

    fun observeVaults(): Flow<List<Vaults>>
    fun getVaultsPaged(limit: Long, offset: Long): Flow<List<Vaults>>

    suspend fun insertVaultAndGetId(name: String, mountPoint: String, dataDir: String): Long

    suspend fun updateVault(id: Long, name: String, mountPoint: String, dataDir: String)

    suspend fun deleteVault(id: Long)

}
