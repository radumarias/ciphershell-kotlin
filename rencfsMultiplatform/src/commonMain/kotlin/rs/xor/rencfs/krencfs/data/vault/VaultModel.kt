package rs.xor.rencfs.krencfs.data.vault

data class VaultModel(
    val id: String?,
    val name: String,
    val mountPoint: String,
    val dataDir: String,
    val password: String? = null,
)
