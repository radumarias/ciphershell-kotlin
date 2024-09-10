import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.kmpbom)
    alias(deps.plugins.jetbrains.compose.compiler)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
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

                implementation(deps.jetbrains.compose.material.navigation)
//                implementation(deps.jetbrains.androidx.navigation)

                implementation(deps.bundles.common.filekit)

                implementation(deps.bundles.common.sqldelight)

                implementation(deps.coroutines)
                implementation(project(":rencfsMultiplatform"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(deps.sqldelight.driver.jvm)
            }
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

