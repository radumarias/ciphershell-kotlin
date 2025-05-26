package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    fun observeVaults(): Flow<Map<String, VaultModel>>

    fun getVaultsPaged(
        limit: Long,
        offset: Long,
    ): Flow<Map<Long, VaultModel>>

    fun getVault(id: Long): VaultModel?

    suspend fun addVault(
        name: String = "",
        mountPoint: String = "",
        dataDir: String = "",
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
    ): String

    suspend fun updateVault(
        id: String,
        name: String,
        mountPoint: String,
        dataDir: String,
    )

    suspend fun deleteVault(id: String)

    fun count(): Long
}
