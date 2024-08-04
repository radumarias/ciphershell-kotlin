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

// Retrieve the current user home
val currentUserHome = System.getProperty("user.home")

compose.desktop {
    application {
        mainClass = "rs.xor.rencfs.krencfs.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "rs.xor.rencfs.krencfs"
            packageVersion = "1.0.1"
        }

        // Set JVM options
        jvmArgs("-Djava.library.path=../../rencfs/java-bridge/target/release/")

        // Set program arguments
        args("$currentUserHome/rencfs/mnt", "$currentUserHome/rencfs/data", "a")
    }
}

// Task to run 'make' in the java-bridge
tasks.register<Exec>("runMake") {
    group = "build"
    description = "Run make in the java-bridge directory"

    workingDir = file("../../rencfs/java-bridge")
    commandLine = listOf("make")
}
