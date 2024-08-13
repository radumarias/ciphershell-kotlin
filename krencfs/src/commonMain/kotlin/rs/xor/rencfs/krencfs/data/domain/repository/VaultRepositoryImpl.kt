package rs.xor.rencfs.krencfs.data.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.xor.rencfs.krencfs.data.domain.model.VaultDataModel

class VaultRepositoryImpl(
    private val localDataSource: VaultLocalDataSource
) : VaultRepository {

    override fun observeVaults(): Flow<Map<Long, VaultDataModel>> {
        return localDataSource.observeVaults().map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id }, { it.toVaultDataModel() })
        }
    }

    override suspend fun addVault(name: String, mountPoint: String, dataDir: String): String {
        val lastInsertId = localDataSource.insertVaultAndGetId(name, mountPoint, dataDir)
        return lastInsertId.toString()
    }

    override suspend fun updateVault(id: String, name: String, mountPoint: String, dataDir: String) {
        val vaultId = id.toLongOrNull()
        if (vaultId != null) {
            localDataSource.updateVault(vaultId, name, mountPoint, dataDir)
        } else {
            throw IllegalArgumentException("Invalid vault ID")
        }
    }

    override suspend fun deleteVault(id: String) {
        val vaultId = id.toLongOrNull()
        if (vaultId != null) {
            localDataSource.deleteVault(vaultId)
        } else {
            throw IllegalArgumentException("Invalid vault ID")
        }
    }

    override fun getVaultsPaged(limit: Long, offset: Long): Flow<Map<Long, VaultDataModel>> {
        return localDataSource.getVaultsPaged(limit, offset).map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id }, { it.toVaultDataModel() })
        }
    }
}

