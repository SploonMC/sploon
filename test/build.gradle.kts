import io.github.sploonmc.sploon.minecraft.MappingType

plugins {
    kotlin("jvm")
    id("io.github.sploonmc.sploon")
}

repositories {
    mavenCentral()
}

dependencies {
    sploon.minecraft("1.21.3", MappingType.Mojang)
}