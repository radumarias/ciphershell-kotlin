package rs.xor.rencfs.krencfs.di

import org.koin.dsl.module
import rs.xor.rencfs.krencfs.utils.UrlLauncher

val uiModule = module {
    single<UrlLauncher> { get<UrlLauncher>() }
}
