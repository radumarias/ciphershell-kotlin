buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    listOf(
        deps.plugins.jetbrains.compose.kmpbom,
        deps.plugins.jetbrains.compose.compiler,
        deps.plugins.jetbrains.kotlin.multiplatform,
        deps.plugins.jetbrains.kotlin.serialization,
        deps.plugins.sqldelight,
        deps.plugins.google.android.application,
        deps.plugins.google.android.hilt,
        deps.plugins.google.android.library,
        deps.plugins.google.ksp,
        deps.plugins.jetbrains.kotlin.android,
        deps.plugins.jetbrains.kotlin.parcelize,
        deps.plugins.mozilla.rust.android.plugin,
        deps.plugins.spotless,
    ).forEach { alias(it) apply false }
}
