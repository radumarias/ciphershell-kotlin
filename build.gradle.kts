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
        deps.plugins.google.android.library,
        deps.plugins.google.ksp,
        deps.plugins.jetbrains.kotlin.android,
        deps.plugins.jetbrains.kotlin.parcelize,
        deps.plugins.mozilla.rust.android.plugin,
        deps.plugins.spotless,
        deps.plugins.detekt
    ).forEach { alias(it) apply false }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    afterEvaluate {
        configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            toolVersion = deps.versions.detekt.get()
            source = files("src")
            parallel = true
        }
    }
    // Tie spotlessCheck to build task for each module
    tasks.named("build") {
        dependsOn(tasks.named("spotlessCheck"))
    }
}

tasks.register("checkAllSpotless") {
    group = "verification"
    description = "Runs spotlessCheck on all modules"
    dependsOn(subprojects.map { "${it.name}:spotlessCheck" })
}

tasks.register("checkAllDetekt") {
    group = "verification"
    description = "Runs Detekt on all modules"
    dependsOn(subprojects.map { "${it.name}:detekt" })
}