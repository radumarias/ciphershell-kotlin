plugins {
    alias(deps.plugins.google.android.application)
    alias(deps.plugins.jetbrains.compose.compiler)
    alias(deps.plugins.jetbrains.kotlin.android)
    alias(deps.plugins.google.ksp)
    alias(deps.plugins.google.hilt)
}
val packageId = "rs.xor.io.rencfs.krencfs"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }


    sourceSets {
        getByName("debug")
        {
            java.srcDir("src/main/preview")
        }
    }


    dependencies {
        coreLibraryDesugaring(androidDeps.jdk.desugaring)
        ksp(androidDeps.hilt.compiler)

        implementation(project(":rencfsMultiplatform"))

        implementation(androidDeps.activity.compose)
        implementation(androidDeps.bundles.appcompat)

        implementation(androidDeps.bundles.compose)

        // hilt
        implementation(androidDeps.bundles.hilt)

    }
}
