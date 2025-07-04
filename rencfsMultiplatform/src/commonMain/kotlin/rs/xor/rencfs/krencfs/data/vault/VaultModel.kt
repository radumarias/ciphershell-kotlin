package rs.xor.rencfs.krencfs.data.vault

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class VaultModel(
    val id: String?,
    val name: String,
    val mountPoint: String,
    val dataDir: String,
    val uri: String? = null,
    val password: String? = null,
    val configureAdvancedSettings: Boolean = false,
    val encryptionAlgorithm: String? = null, // "ChaCha20Poly1305" or "AES-GCM"
    val keySize: String? = null, // "128-bit", "192-bit", "256-bit"
    val recoveryCode: String? = null,
    val isLocked: Boolean = true,
)
