
plugins {
    kotlin("jvm") version "2.2.20"
    id("io.kotest") version "6.1.0"
    `jvm-test-suite`
    idea
    application
    id("com.gradleup.shadow") version "9.3.1"
}

group = "com.rewe.digital.gradle"
version = "1.0-SNAPSHOT"
val MAIN_CLASS_NAME = "de.chasenet.ic10.CLIKt"

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
    testImplementation("io.kotest:kotest-framework-engine:6.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:6.1.0")
}

tasks.test {
    outputs.upToDateWhen { false }
    useJUnitPlatform()
}

application {
    mainClass = MAIN_CLASS_NAME
}

tasks.register<JavaExec>("runApp") {
    group = "build"
    classpath = sourceSets.main.get().runtimeClasspath

    mainClass = MAIN_CLASS_NAME
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = MAIN_CLASS_NAME
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }

    archiveFileName = "${project.name}.jar"
}

distributions {
    this.shadow {
        this.distributionBaseName = project.name
    }
}

val copyJarToDist by tasks.registering(Copy::class) {
    dependsOn(tasks.shadowJar, tasks.shadowDistZip)
    from(tasks.shadowJar.map { it.archiveFile })
    into(tasks.shadowDistZip.flatMap { it.destinationDirectory })
}

tasks.assembleShadowDist {
    finalizedBy(copyJarToDist)
}

tasks.jar {
    enabled = false
}

kotlin {
    jvmToolchain(11)
}