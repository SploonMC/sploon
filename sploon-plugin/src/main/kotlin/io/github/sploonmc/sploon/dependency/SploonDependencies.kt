package io.github.sploonmc.sploon.dependency

import io.github.sploonmc.sploon.SPIGOT_API_DEPENDENCY
import io.github.sploonmc.sploon.compileOnly
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.Remapper
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.patcher.Patcher
import org.gradle.api.Project
import kotlin.io.path.Path

object SploonDependencies {
    fun <P : MappingProvider<MappingType<P>>> handleMinecraft(
        project: Project,
        version: MinecraftVersion,
        mappingType: MappingType<P>,
    ) {
        val patcher = Patcher(project, version)
        patcher.download()
        if (!patcher.isCached) patcher.patch()

        patcher.installLibraries()
        val cacheDir = Path(project.gradle.gradleUserHomeDir.path, "caches/sploon")
        val mapping = mappingType.providerFactory().getMappingFor(project, version, mappingType)
        val remapper = Remapper(
            cacheDir.resolve("mappings"),
            patcher.spigotJar,
            cacheDir.resolve("final/minecraft-$version-$mappingType.jar"),
            mappingType,
            mapping.toPath(),
            version,
            mappingType.reversed
        )

        remapper.remap()

        project.compileOnly(project.files(remapper.output))
    }

    fun handleSpigot(
        project: Project,
        version: MinecraftVersion
    ) {
        project.compileOnly(SPIGOT_API_DEPENDENCY.replace("<version>", version.toString()))
    }
}
