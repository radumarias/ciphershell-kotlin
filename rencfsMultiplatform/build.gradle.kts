import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.kmpbom)
    alias(deps.plugins.jetbrains.compose.compiler)
    alias(deps.plugins.jetbrains.kotlin.serialization)
    alias(deps.plugins.sqldelight)
    //    alias(deps.plugins.jetbrains.kotlin.parcelize)
// Android
    alias(deps.plugins.google.android.library)
//    alias(deps.plugins.jetbrains.kotlin.android)
    alias(deps.plugins.spotless)
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
            resources.srcDir("src/commonMain/res")

            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.runtimeSaveable)
                api(compose.ui)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.components.resources)
                api(compose.animation)
                api(compose.animationGraphics)
                api(compose.material3AdaptiveNavigationSuite)

//                todo: not yet supported in kmp
//                api(compose.preview)
//                implementation(compose.components.uiToolingPreview)

                implementation(deps.jetbrains.kotlinx.serialization.json)
                implementation(deps.jetbrains.androidx.navigation.compose)
                implementation(deps.jetbrains.androidx.lifecycle.runtime.compose)
                implementation(deps.jetbrains.androidx.lifecycle.viewmodel.compose)

                implementation(deps.bundles.common.filekit)
                implementation(deps.bundles.common.sqldelight)

                api(deps.coroutines)

                implementation(project(":rencfsWrapper"))
                implementation(deps.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                api(deps.sqldelight.driver.android)
                implementation(deps.koin.android)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                api(deps.sqldelight.driver.jvm)
            }
        }
        // Define jvmMain source set
        val jvmMain by creating {
            dependsOn(commonMain.get())
        }

        // Link jvmMain to Android and Desktop
        androidMain.dependsOn(jvmMain)
        desktopMain.dependsOn(jvmMain)
    }
}

//val currentUserHome: String? = System.getProperty("user.home")
val applicationPackageName = project.findProperty("RENCFS_PACKAGE_NAME") as String?

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
//        args("$currentUserHome/rencfs/mnt", "$currentUserHome/rencfs/data", "a")
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
    sourceSets {
        getByName("debug")
        {
            java.srcDir("src/androidMain/preview")
        }
    }
    dependencies {
        implementation(androidDeps.compose.tooling)
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

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.5.0")
            .editorConfigOverride(
                mapOf("ktlint_function_naming_ignore_when_annotated_with" to "Composable")
            )
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.named<Delete>("clean") {
    delete("../rencfs/java-bridge/target")
}
