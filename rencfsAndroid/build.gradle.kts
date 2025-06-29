val packageId = "rs.xor.io.rencfs.krencfs"

plugins {
    alias(deps.plugins.google.android.application)
    alias(deps.plugins.google.ksp)
    alias(deps.plugins.jetbrains.compose.compiler)
    alias(deps.plugins.jetbrains.kotlin.android)
    alias(deps.plugins.mozilla.rust.android.plugin)
    alias(deps.plugins.spotless)
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

android {
    namespace = packageId
    compileSdk = 36

    ndkVersion = "27.2.12479018"

    defaultConfig {
        applicationId = packageId
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        coreLibraryDesugaring(androidDeps.jdk.desugaring)

        implementation(project(":rencfsMultiplatform"))

        implementation(deps.sqldelight.driver.android)
        implementation(deps.bundles.common.filekit)

        implementation(androidDeps.activity.compose)

        implementation(androidDeps.bundles.appcompat)
        implementation(androidDeps.bundles.compose)

        implementation(deps.koin.android)
    }
}

cargo {
    verbose = true
    module = "../rencfs/java-bridge"
    libname = "java_bridge"
    targets = listOf("arm64", "x86_64")
    apiLevel = 21
    profile = "release"
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.5.0")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("1.5.0")
    }
}

tasks.matching { it.name in listOf("javaPreCompileDebug", "javaPreCompileRelease") }.configureEach {
    dependsOn("cargoBuild")
}

tasks.matching { it.name == "build" }.configureEach {
    if (tasks.findByName("spotlessCheck") != null) {
        dependsOn(tasks.named("spotlessCheck"))
    }
}
