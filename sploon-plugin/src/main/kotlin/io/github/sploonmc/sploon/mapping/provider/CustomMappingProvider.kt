package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.format.MappingFormat
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project

class CustomMappingProvider<F : MappingFormat<F>> : MappingProvider<MappingType<CustomMappingProvider<F>, F>, F> {
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<CustomMappingProvider<F>, F>
    ) = (type as MappingType.Custom<F>).mappingsFile

    override fun name() = "custom"
}