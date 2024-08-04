buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        /*TODO*/
    }
}

plugins {

    alias(deps.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(deps.plugins.jetbrains.compose.framework) apply false
    alias(deps.plugins.jetbrains.compose.interop) apply false
//    alias(deps.plugins.jetbrains.kotlin.kapt) apply false
//    alias(deps.plugins.jetbrains.kotlin.dokka) apply false

//    alias(deps.plugins.jetbrains.kotlin.serialization) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
