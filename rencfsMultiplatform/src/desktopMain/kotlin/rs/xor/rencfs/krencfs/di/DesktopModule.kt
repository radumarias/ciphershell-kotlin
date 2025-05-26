package rs.xor.rencfs.krencfs.di

import org.koin.dsl.module
import rs.xor.rencfs.krencfs.utils.DesktopFileOpener
import rs.xor.rencfs.krencfs.utils.DesktopUrlLauncher
import rs.xor.rencfs.krencfs.utils.FileOpener
import rs.xor.rencfs.krencfs.utils.UrlLauncher

val desktopModule =
    module {
        single<UrlLauncher> { DesktopUrlLauncher() }
        single<FileOpener> { DesktopFileOpener() }
    }
