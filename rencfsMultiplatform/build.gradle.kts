import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.kmpbom)
    alias(deps.plugins.jetbrains.compose.compiler)
//    alias(deps.plugins.jetbrains.kotlin.serialization)
    alias(deps.plugins.sqldelight)
    //    alias(deps.plugins.jetbrains.kotlin.parcelize)
// Android
    alias(deps.plugins.google.android.library)
//    alias(deps.plugins.jetbrains.kotlin.android)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        androidTarget {
            compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvmToolchain(17)
    jvm("desktop")

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain {
//            kotlin.srcDir("commonMain/kotlin")
            resources.srcDir("src/commonMain/res")

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(compose.runtimeSaveable)
                api(compose.ui)
//                api(compose.uiUtil)
//                api(compose.uiTooling)
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.preview)
                implementation(compose.components.uiToolingPreview)
                api(compose.components.resources)
                api(compose.animation)
                api(compose.animationGraphics)

//                implementation(deps.jetbrains.compose.material.navigation)
//                implementation(deps.jetbrains.androidx.navigation)

                implementation(deps.bundles.common.filekit)

                implementation(deps.bundles.common.sqldelight)

                api(deps.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                api(deps.sqldelight.driver.android)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                api(deps.sqldelight.driver.jvm)
            }
        }
    }
}

val currentUserHome: String? = System.getProperty("user.home")
val applicationPackageName = "rs.xor.rencfs.krencfs"


compose.desktop {
    application {
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


android {
    namespace = applicationPackageName
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = 35
    lint {
        targetSdk = 35
    }
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
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
//    we don't build native targets yet
//    linkSqlite = true
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
