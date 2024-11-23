package io.github.sploonmc.sploon.dependency

import io.github.sploonmc.sploon.SPIGOT_API_DEPENDENCY
import io.github.sploonmc.sploon.compileOnly
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.format.MappingFormat
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.patcher.Patcher
import org.gradle.api.Project

object SploonDependencies {
    fun <P : MappingProvider<MappingType<P, F>, F>, F : MappingFormat<F>> handleMinecraft(
        project: Project,
        version: MinecraftVersion,
        mappingType: MappingType<P, F>,
    ) {
        val patcher = Patcher(project, version)
        patcher.download()
        if (!patcher.isCached) patcher.patch()

        patcher.installLibraries()

        project.compileOnly(project.files(patcher.spigotJar))
    }

    fun handleSpigot(
        project: Project,
        version: MinecraftVersion
    ) {
        project.compileOnly(SPIGOT_API_DEPENDENCY.replace("<version>", version.toString()))
    }
}