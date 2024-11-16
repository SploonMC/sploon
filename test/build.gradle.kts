import io.github.sploonmc.sploon.minecraft.MappingType

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
    sploon.minecraft("1.21.3", MappingType.Mojang)
}