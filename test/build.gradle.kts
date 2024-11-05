plugins {
    kotlin("jvm")
    id("sploon")
}

fun prop(prop: String) = project.rootProject.property(prop) as String

group = prop("group")
version = prop("version")

repositories {
    mavenCentral()
}

dependencies {
    sploon.spigot("1.21.33")
}