package rs.xor.rencfs.krencfs.di

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import rs.xor.rencfs.krencfs.KrenkfsDB
import rs.xor.rencfs.krencfs.VaultsQueries
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightVaultDAO
import rs.xor.rencfs.krencfs.data.sqldelight.provideSQLDriver
import rs.xor.rencfs.krencfs.data.vault.VaultDAO
import rs.xor.rencfs.krencfs.data.vault.VaultRepository
import rs.xor.rencfs.krencfs.data.vault.VaultRepositoryImpl

val sqlDelightModule = module {
    single<SqlDriver> {
        runBlocking { provideSQLDriver(KrenkfsDB.Schema, "Vaults") }
    }
    single { KrenkfsDB(get()) }
    single<VaultsQueries> { get<KrenkfsDB>().vaultsQueries }
    single<VaultDAO> { SQLDelightVaultDAO(get(), Dispatchers.IO) }
    single<VaultRepository> { VaultRepositoryImpl(get()) }
}
