package rs.xor.rencfs.krencfs.data.sqldelight.di

import android.content.Context
import org.koin.dsl.module

val androidModule = module {
    single<Context> { get<android.app.Application>() } // Koin provides Application context
}
