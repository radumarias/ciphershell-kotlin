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
//    alias(deps.plugins.jetbrains.kotlin.parcelize) apply false
//    alias(deps.plugins.jetbrains.kotlin.serialization) apply false
    alias(deps.plugins.sqldelight) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
