package rs.xor.rencfs.krencfs.data.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.data.domain.model.VaultDataModel

interface VaultRepository {
    fun observeVaults(): Flow<Map<Long, VaultDataModel>>
    suspend fun addVault(name: String, mountPoint: String, dataDir: String): String
    suspend fun updateVault(id: String, name: String, mountPoint: String, dataDir: String)
    suspend fun deleteVault(id: String)
    fun getVaultsPaged(limit: Long, offset: Long): Flow<Map<Long, VaultDataModel>>
}
