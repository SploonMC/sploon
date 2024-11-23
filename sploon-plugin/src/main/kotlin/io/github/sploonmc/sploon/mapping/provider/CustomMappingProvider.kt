package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project

class CustomMappingProvider : MappingProvider<MappingType<CustomMappingProvider>> {
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<CustomMappingProvider>
    ) = (type as MappingType.Custom).mappingsFile

    override fun name() = "custom"
}