plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "rencfs-kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("java_bridge"))
}

application {
    mainClass.set("MainKt")
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
