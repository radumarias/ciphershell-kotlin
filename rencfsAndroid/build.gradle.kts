
plugins {
    alias(deps.plugins.google.android.application)
    alias(deps.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "rs.xor.io.rencfs"
    compileSdk = 35

    defaultConfig {
        applicationId = "rs.xor.io.rencfs"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"
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
        project(":rencfsMultiplatform")
//        implementation(androidDeps.)
    }
}
