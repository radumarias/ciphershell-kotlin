package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow

interface VaultDAO {
    fun observeVaults(): Flow<List<VaultModel>>

    fun getVaultsPaged(
        limit: Long,
        offset: Long,
    ): Flow<List<VaultModel>>

    fun getVaultById(id: Long): VaultModel?

    suspend fun insertVaultAndGetId(
        name: String,
        mountPoint: String,
        dataDir: String,
        uri: String?,
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
        isLocked: Long,
    ): Long

    suspend fun updateVault(
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
    )

    suspend fun deleteVault(id: Long)

    fun count(): Long
}
