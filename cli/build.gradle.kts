plugins {
    kotlin("jvm")
    application
}

group = "krencfs-cli"
version = "0.1.0"

application {
    mainClass.set("MainKt")

    // Set JVM options
    applicationDefaultJvmArgs = listOf("-Djava.library.path=../rencfs/java-bridge/target/release/")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(sourceSets.main.get().output)
}

// Task to build java-bridge
tasks.register<Exec>("buildRust") {
    group = "build"
    description = "Build java-bridge"

    workingDir = file("../rencfs/java-bridge")
    commandLine = listOf("cargo", "build", "--release")
}

// Retrieve the current user home
val currentUserHome: String? = System.getProperty("user.home")

tasks.named<JavaExec>("run") {
    // Set program arguments
    args = listOf("$currentUserHome/rencfs/mnt", "$currentUserHome/rencfs/data", "a")

    dependsOn("buildRust")
}

// Make the build task depend on the buildRust task
tasks.named("build") {
    dependsOn("buildRust")
}
