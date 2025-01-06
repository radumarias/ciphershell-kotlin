package rs.xor.rencfs.krencfs.data.vault

import kotlinx.coroutines.flow.map

class VaultRepositoryImpl(
    private val dao: VaultDAO
) : VaultRepository {

    override fun observeVaults() = dao.observeVaults().map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id.toString() }, { it })
        }

    override fun getVaultsPaged(limit: Long, offset: Long) =  dao.getVaultsPaged(limit, offset).map { vaultsList ->
            vaultsList.associateByTo(LinkedHashMap(), { it.id!!.toLong() }, { it })
        }

    override fun getVault(id: Long): VaultModel? = dao.getVaultById(id)

    override suspend fun addVault(name: String, mountPoint: String, dataDir: String) = dao.insertVaultAndGetId(name, mountPoint, dataDir).toString()

    override suspend fun updateVault(id: String, name: String, mountPoint: String, dataDir: String) {
        val vaultId = id.toLongOrNull()
        if (vaultId != null) {
            println("VaultRepositoryImpl Update vault id: $id, name: $name, mountPoint: $mountPoint, dataDir: $dataDir")
            dao.updateVault(vaultId, name, mountPoint, dataDir)
        } else {
            throw IllegalArgumentException("Invalid vault ID")
        }
    }

    override suspend fun deleteVault(id: String) = id.toLongOrNull()?.let { dao.deleteVault(it) } ?: Unit

    override  fun count() =  dao.count()

}
