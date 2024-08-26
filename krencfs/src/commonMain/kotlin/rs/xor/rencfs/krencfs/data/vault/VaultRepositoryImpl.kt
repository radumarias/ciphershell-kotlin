package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.xor.rencfs.krencfs.data.vault.sqldelight.toVaultDataModel

class VaultRepositoryImpl(
    private val dao: VaultDAO
) : VaultRepository {

    override fun observeVaults(): Flow<Map<String, VaultModel>> {
        return dao.observeVaults().map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id.toString() }, { it.toVaultDataModel() })
        }
    }

    override suspend fun addVault(name: String, mountPoint: String, dataDir: String): String {
        val lastInsertId = dao.insertVaultAndGetId(name, mountPoint, dataDir)
        return lastInsertId.toString()
    }

    override suspend fun updateVault(id: String, name: String, mountPoint: String, dataDir: String) {
        val vaultId = id.toLongOrNull()
        if (vaultId != null) {
            println("VaultRepositoryImpl Update vault id: $id, name: $name, mountPoint: $mountPoint, dataDir: $dataDir")
            dao.updateVault(vaultId, name, mountPoint, dataDir)
        } else {
            throw IllegalArgumentException("Invalid vault ID")
        }
    }

    override suspend fun deleteVault(id: String) {
        val vaultId = id.toLongOrNull()
        if (vaultId != null) {
            dao.deleteVault(vaultId)
        } else {
            throw IllegalArgumentException("Invalid vault ID")
        }
    }

    override fun getVaultsPaged(limit: Long, offset: Long): Flow<Map<Long, VaultModel>> {
        return dao.getVaultsPaged(limit, offset).map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id }, { it.toVaultDataModel() })
        }
    }
}

