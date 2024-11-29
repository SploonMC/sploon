package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import java.io.File

class CustomMappingProvider(val mappingsFile: File) : MappingProvider<MappingType<CustomMappingProvider>> {
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<CustomMappingProvider>
    ) = mappingsFile

    override fun name() = mappingsFile.nameWithoutExtension
}