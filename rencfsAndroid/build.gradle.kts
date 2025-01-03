val packageId = "rs.xor.io.rencfs.krencfs"

plugins {
    alias(deps.plugins.google.android.application)
    alias(deps.plugins.jetbrains.compose.compiler)
    alias(deps.plugins.jetbrains.kotlin.android)
    alias(deps.plugins.google.ksp)
    alias(deps.plugins.google.hilt)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = packageId
    compileSdk = 35

    hilt {
        enableAggregatingTask = true
    }

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

    kotlinOptions {
        jvmTarget = "17"
    }

    dependencies {
        coreLibraryDesugaring(androidDeps.jdk.desugaring)
        ksp(androidDeps.hilt.compiler)

        implementation(project(":rencfsMultiplatform"))

        implementation(deps.sqldelight.driver.android)
        implementation(deps.bundles.common.filekit)

        implementation(androidDeps.activity.compose)

        implementation(androidDeps.bundles.appcompat)
        implementation(androidDeps.bundles.compose)

        // hilt
        implementation(androidDeps.bundles.hilt)
    }
}
