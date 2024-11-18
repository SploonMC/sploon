plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.5"
    `java-gradle-plugin`
}

fun prop(prop: String) = project.rootProject.property(prop) as String

group = prop("group")
version = prop("version")

gradlePlugin {
    plugins {
        create("sploon") {
            id = "sploon"
            implementationClass = "io.github.sploonmc.sploon.SploonPlugin"
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
}