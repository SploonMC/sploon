package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import java.io.File

object NoopMappingProvider : MappingProvider<MappingType<NoopMappingProvider>> {
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<NoopMappingProvider>
    ): File {
        TODO()
    }

    override fun name() = "no-op"
}