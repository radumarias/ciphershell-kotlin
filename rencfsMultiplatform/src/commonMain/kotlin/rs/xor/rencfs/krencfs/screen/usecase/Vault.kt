package rs.xor.rencfs.krencfs.screen.usecase

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.StateFlow
import rs.xor.rencfs.krencfs.data.vault.VaultModel

interface OnCreateVaultUseCase : UseCase

@Immutable
data class SelectVaultUseCaseParams(
    val vaultId: String? = null,
) : UseCaseParams

interface OnVaultSelectedUseCase : ParametrizedUseCase<SelectVaultUseCaseParams>

interface VaultListScreenState {
    val vaults: StateFlow<Map<String, VaultModel>>
    val firstStart: Boolean
}

interface VaultListScreenUseCase {
    val onCreateVault: OnCreateVaultUseCase
    val onVaultSelected: OnVaultSelectedUseCase
}
