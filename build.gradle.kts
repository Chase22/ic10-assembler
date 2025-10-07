plugins {
    kotlin("jvm") version "2.1.21"
    id("io.kotest") version "6.0.0.M4"
    `jvm-test-suite`
    idea
}

group = "com.rewe.digital.gradle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.3")
    testImplementation("io.kotest:kotest-framework-engine:6.0.0.M4")
    testImplementation("io.kotest:kotest-runner-junit5:6.0.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runApp") {
    group = "build"
    classpath = sourceSets.main.get().runtimeClasspath

    mainClass = "de.chasenet.ic10.CLIKt"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "de.chasenet.ic10.CLIKt"
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    archiveFileName = "${project.name}.jar"
}

/*tasks.processResources {
    filesMatching("version.txt") {
        expand("version" to project.version)
    }
}*/

kotlin {
    jvmToolchain(21)
}