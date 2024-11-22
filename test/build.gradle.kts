import io.github.sploonmc.sploon.minecraft.MappingType

plugins {
    id("java")
    id("io.github.sploonmc.sploon")
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    sploon.minecraft("1.21.3", MappingType.Mojang)
    sploon.pluginImplementation(files("luckperms.jar"))
}