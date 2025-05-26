package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow
import rs.xor.rencfs.krencfs.Vault

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
        uri: String?,
        configureAdvancedSettings: Long,
        encryptionAlgorithm: String?,
        keySize: String?,
        recoveryCode: String?,
        isLocked: Long,
    ): String

    suspend fun updateVault(vault: Vault)

    suspend fun deleteVault(id: String)

    fun count(): Long
}
