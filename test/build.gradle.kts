import io.github.sploonmc.sploon.mapping.MappingType

plugins {
    id("java")
    id("io.github.sploonmc.sploon")
}

dependencies {
    sploon.minecraft("1.21.3", MappingType.Spigot)
    sploon.pluginImplementation(files("luckperms.jar"))
}