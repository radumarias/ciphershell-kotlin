package rs.xor.rencfs.krencfs.di

import android.content.Context
import org.koin.dsl.module
import rs.xor.rencfs.krencfs.utils.AndroidFileOpener
import rs.xor.rencfs.krencfs.utils.AndroidUrlLauncher
import rs.xor.rencfs.krencfs.utils.FileOpener
import rs.xor.rencfs.krencfs.utils.UrlLauncher

val androidModule = module {
    single<Context> { get<android.app.Application>() } // Koin provides Application context
    single<UrlLauncher> { AndroidUrlLauncher(get()) }
    single<FileOpener> { AndroidFileOpener() }
}
