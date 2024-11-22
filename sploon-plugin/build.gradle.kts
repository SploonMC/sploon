plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.5"
    id("com.gradle.plugin-publish") version "1.2.1"
    `java-gradle-plugin`
}

// Need to have these set to compile-time constants
// because the gradle publishing plugin does not like
// using properties or something.
group = "io.github.sploonmc"
version = "0.1.0"

tasks.shadowJar {
    archiveClassifier = ""
}

gradlePlugin {
    vcsUrl = "https://github.com/SploonMC/Sploon.git"
    website = "https://github.com/SploonMC/Sploon"

    plugins {
        create("sploon") {
            id = "io.github.sploonmc.sploon"
            implementationClass = "$id.SploonPlugin"
            displayName = "SploonMC"
            description = "A gradle plugin making Minecraft development easier"
            tags = setOf("minecraft")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.sigpipe:jbsdiff:1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.gradleup.shadow:shadow-gradle-plugin:8.3.5")
}

publishing {
    repositories {
        maven {
            name = "sploon"
            url = uri("https://maven.radsteve.net/sploon")

            credentials {
                username = System.getenv("SPLOON_MAVEN_USER")
                password = System.getenv("SPLOON_MAVEN_TOKEN")
            }
        }
    }
}