package rs.xor.rencfs.krencfs.screen.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.data.vault.VaultRepository

class VaultListScreenStateImpl(
    firstTime: Boolean,
) : VaultListScreenState,
    KoinComponent {
    private val _vaults = MutableStateFlow<Map<String, VaultModel>>(emptyMap())
    override val vaults: StateFlow<Map<String, VaultModel>> = _vaults.asStateFlow()
    override val firstStart: Boolean = firstTime
    private val vaultRepository: VaultRepository by inject()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            vaultRepository.observeVaults().collect { vaults ->
                _vaults.value = vaults
            }
        }
    }
}

class VaultListScreenUseCaseImpl(
    override val onCreateVault: OnCreateVaultUseCase,
    override val onVaultSelected: OnVaultSelectedUseCase,
) : VaultListScreenUseCase
