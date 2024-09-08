
plugins {
    alias(deps.plugins.google.android.application)
    alias(deps.plugins.jetbrains.kotlin.android)
    alias(deps.plugins.jetbrains.compose.interop)
    alias(deps.plugins.jetbrains.compose.framework)
    alias(androidDeps.plugins.hilt)
    alias(androidDeps.plugins.ksp)
}

android {
    namespace = "rs.xor.io.rencfs.krencfs"
    compileSdk = 35

    defaultConfig {
        applicationId = "rs.xor.io.rencfs"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dependencies {
        implementation(project(":rencfsMultiplatform"))
        implementation(androidDeps.activity.compose)
        implementation(androidDeps.bundles.appcompat)

        implementation(androidDeps.bundles.compose)


        // compose

        // hilt
        implementation(androidDeps.hilt.android)
        implementation(androidDeps.androidx.hilt.navigation.compose)
        ksp(androidDeps.hilt.compiler)
    }
}
