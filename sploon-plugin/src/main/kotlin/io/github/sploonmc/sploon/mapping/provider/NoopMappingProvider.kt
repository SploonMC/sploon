package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.format.TinyV2MappingFormat
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import java.io.File

class NoopMappingProvider : MappingProvider<MappingType<NoopMappingProvider, TinyV2MappingFormat>, TinyV2MappingFormat> {
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<NoopMappingProvider, TinyV2MappingFormat>
    ): File {
        TODO()
    }

    override fun name() = "no-op"
}