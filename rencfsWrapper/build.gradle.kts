import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.google.android.library)
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
                api(deps.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val desktopMain by getting {
            dependencies {
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

android {
    namespace = project.findProperty("RENCFS_PACKAGE_NAME") as String?
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

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.5.0")
            .editorConfigOverride(
                mapOf("ktlint_function_naming_ignore_when_annotated_with" to "Composable"),
            )
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("1.5.0")
    }
}

tasks.named<Delete>("clean") {
    delete("../rencfs/java-bridge/target")
}

tasks.matching { it.name == "build" }.configureEach {
    if (tasks.findByName("spotlessCheck") != null) {
        dependsOn(tasks.named("spotlessCheck"))
    }
}
