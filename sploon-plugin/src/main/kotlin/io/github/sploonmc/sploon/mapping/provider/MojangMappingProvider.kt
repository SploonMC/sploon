package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.SploonPlugin.Companion.PISTON_VERSIONS
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.piston.getVersionMeta
import org.gradle.api.Project
import java.net.URI

object MojangMappingProvider : DownloadingMappingProvider<MappingType<MojangMappingProvider>> {
    override fun getDownloadedFile(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<MojangMappingProvider>
    ) = cacheDir(project).resolve("$version.txt").toFile()

    override fun getURI(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<MojangMappingProvider>
    ) = URI(PISTON_VERSIONS.getVersionMeta(version).downloads.serverMappings.url)

    override fun name() = "mojang"
}