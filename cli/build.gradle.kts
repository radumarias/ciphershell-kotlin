plugins {
    kotlin("jvm")
    application
}

group = "rencfs-kotlin"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("MainKt")

    // Set JVM options
    applicationDefaultJvmArgs = listOf("-Djava.library.path=../../rencfs/java-bridge/target/release/")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt" // Replace with your main class
    }
    from(sourceSets.main.get().output)
}

// Task to run 'make' in the java-bridge
tasks.register<Exec>("runMake") {
    group = "build"
    description = "Run make in the java-bridge directory"

    workingDir = file("../../rencfs/java-bridge")
    commandLine = listOf("make")
}

// Retrieve the current user home
val currentUserHome = System.getProperty("user.home")

tasks.named<JavaExec>("run") {
    // Set program arguments
    args = listOf("$currentUserHome/rencfs/mnt", "$currentUserHome/rencfs/data", "a")
}