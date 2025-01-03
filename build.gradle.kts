buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(deps.plugins.jetbrains.compose.kmpbom) apply false
    alias(deps.plugins.jetbrains.compose.compiler) apply false
    alias(deps.plugins.jetbrains.kotlin.serialization) apply false
    alias(deps.plugins.sqldelight) apply false
// Android
    alias(deps.plugins.google.android.application) apply false
    alias(deps.plugins.google.android.library) apply false
    alias(deps.plugins.jetbrains.kotlin.android) apply false
    alias(deps.plugins.google.ksp) apply false
    alias(deps.plugins.google.hilt) apply false

    alias(deps.plugins.jetbrains.kotlin.parcelize) apply false
}
