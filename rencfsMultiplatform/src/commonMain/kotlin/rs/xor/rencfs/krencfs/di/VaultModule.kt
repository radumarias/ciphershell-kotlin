package rs.xor.rencfs.krencfs.di

import androidx.navigation.NavHostController
import org.koin.dsl.module
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.OnCreateVaultUseCase
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenStateImpl
import rs.xor.rencfs.krencfs.screen.usecase.OnCreateVaultUseCaseImpl
import rs.xor.rencfs.krencfs.screen.usecase.OnVaultSelectedUseCase
import rs.xor.rencfs.krencfs.screen.usecase.OnVaultSelectedUseCaseImpl
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCaseImpl

val vaultModule = module {
    factory<VaultListScreenState> { (firstTime: Boolean) -> VaultListScreenStateImpl(firstTime) }
    single<OnCreateVaultUseCase> { OnCreateVaultUseCaseImpl(get<NavHostController>()) }
    single<OnVaultSelectedUseCase> { OnVaultSelectedUseCaseImpl(get<NavHostController>()) }
    single<VaultListScreenUseCase> { VaultListScreenUseCaseImpl(get(), get()) }
}
