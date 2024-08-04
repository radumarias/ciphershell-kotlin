import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.framework)
    alias(deps.plugins.jetbrains.compose.interop)
}

kotlin {
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "rs.xor.rencfs.krencfs.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "rs.xor.rencfs.krencfs"
            packageVersion = "1.0.1"
        }
    }
}
