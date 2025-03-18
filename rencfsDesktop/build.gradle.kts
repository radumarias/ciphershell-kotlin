import org.gradle.internal.os.OperatingSystem
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(deps.plugins.jetbrains.kotlin.multiplatform)
    alias(deps.plugins.jetbrains.compose.kmpbom)
    alias(deps.plugins.jetbrains.compose.compiler)
    alias(deps.plugins.spotless)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.animation)

                implementation(deps.bundles.common.filekit)

                implementation(deps.bundles.common.sqldelight)

                implementation(deps.coroutines)
                implementation(project(":rencfsMultiplatform"))
                implementation(deps.koin.core)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(deps.sqldelight.driver.jvm)
            }
        }
    }
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat("1.23.0")
        importOrder("java", "javax", "org", "com", "")
        removeUnusedImports()
    }
    kotlin {
        target("src/**/*.kt")
        ktlint("1.5.0")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
val applicationPackageName = "rs.xor.rencfs.krencfs"
val applicationClassName = "RencfsDesktopApplication"
val mainClassPath = "${applicationPackageName}.${applicationClassName}Kt"

compose.desktop {
    application {
        mainClass = mainClassPath
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = applicationPackageName
            packageVersion = "1.0.0"
        }
    }
}

val rustLibBuildTarget: String = when {
    OperatingSystem.current().isWindows -> "x86_64-pc-windows-msvc"
    OperatingSystem.current().isMacOsX -> when {
        System.getProperty("os.arch") == "aarch64" -> "aarch64-apple-darwin"
        else -> "x86_64-apple-darwin"
    }
    else -> "x86_64-unknown-linux-gnu"
}

val rustLibBaseDir = "${projectDir}/../rencfs/java-bridge"
val rustLibName = when {
    OperatingSystem.current().isWindows -> "java_bridge.dll"
    OperatingSystem.current().isMacOsX -> "libjava_bridge.dylib"
    else -> "libjava_bridge.so"
}

tasks.register<Exec>("buildRencfsRustJavaBridge") {
    group = "build"
    workingDir = file(rustLibBaseDir)
    commandLine = listOf("cargo", "build", "--target", rustLibBuildTarget, "--release")
}

tasks.register<Copy>("copyRencfsJavaBridgeLib") {
    dependsOn("buildRencfsRustJavaBridge")
    from(file("${rustLibBaseDir}/target/${rustLibBuildTarget}/release").resolve(rustLibName))
    into(layout.buildDirectory.dir("../libs"))
}

tasks.named("compileKotlinDesktop") {
    finalizedBy("copyRencfsJavaBridgeLib")
}

tasks.named<Delete>("clean") {
    delete(layout.buildDirectory.dir("../libs"))
}

