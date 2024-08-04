plugins {
    kotlin("jvm")
    application
}

group = "rencfs-kotlin"
version = "1.0-SNAPSHOT"

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
