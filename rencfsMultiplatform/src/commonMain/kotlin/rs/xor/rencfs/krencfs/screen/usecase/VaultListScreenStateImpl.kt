package rs.xor.rencfs.krencfs.screen.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel

class VaultListScreenStateImpl(
    firstTime: Boolean,
) : VaultListScreenState {
    private val _vaults = MutableStateFlow<Map<String, VaultModel>>(emptyMap())
    override val vaults: StateFlow<Map<String, VaultModel>> = _vaults.asStateFlow()
    override val firstStart: Boolean = firstTime

    init {
        CoroutineScope(Dispatchers.IO).launch {
            SQLDelightDB.getVaultRepositoryAsync().observeVaults().collect { vaults ->
                _vaults.value = vaults
            }
        }
    }
}

class VaultListScreenUseCaseImpl(
    override val onCreateVault: OnCreateVaultUseCase,
    override val onVaultSelected: OnVaultSelectedUseCase,
) : VaultListScreenUseCase
