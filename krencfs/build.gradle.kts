import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.framework)
    alias(deps.plugins.jetbrains.compose.interop)
//    alias(deps.plugins.jetbrains.kotlin.parcelize)
//    alias(deps.plugins.jetbrains.kotlin.serialization)
    alias(deps.plugins.sqldelight)
}

kotlin {
    jvm("desktop")
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(deps.sqldelight.runtime)
                implementation(deps.sqldelight.coroutines.extensions)

                implementation(deps.filekit.core)
                implementation(deps.filekit.compose)

                implementation(deps.coroutines) // Kotlin Coroutines
            }
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(deps.sqldelight.driver.jvm)
        }
    }
}

val currentUserHome: String? = System.getProperty("user.home")
val applicationPackageName = "rs.xor.rencfs.krencfs"
val applicationClassName = "KRencfsApplicationKt"
val mainClassPath = "${applicationPackageName}.${applicationClassName}"

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = mainClassPath
    }
}
compose.desktop {
    application {
        mainClass = mainClassPath

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = applicationPackageName
            packageVersion = "1.0.0"
        }

        // Set JVM options
        jvmArgs("-Djava.library.path=../rencfs/java-bridge/target/release/")

        // Set program arguments
        args("$currentUserHome/rencfs/mnt", "$currentUserHome/rencfs/data", "a")
    }
}

sqldelight {
    databases {
        create("KrenkfsDB") {
            packageName.set(applicationPackageName)
            generateAsync.set(true)
            // todo: choose src folders
        }
    }
    linkSqlite = true
}

// Task to build java-bridge
val buildRust by tasks.registering(Exec::class) {
    group = "build"
    description = "Build java-bridge"

    environment("RUST_LOG", "debug")
    workingDir = file("../rencfs/java-bridge")
    commandLine = listOf("cargo", "build", "--release")
}

// Make the build task depend on the buildRust task
tasks.named("build") {
    dependsOn(buildRust)
}
