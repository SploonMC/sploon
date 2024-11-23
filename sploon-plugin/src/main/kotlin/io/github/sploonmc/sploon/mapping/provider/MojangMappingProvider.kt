package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.SploonPlugin.Companion.PISTON_VERSIONS
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.format.ProguardMappingFormat
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.piston.getVersionMeta
import org.gradle.api.Project
import java.net.URI

class MojangMappingProvider : DownloadingMappingProvider<MappingType<MojangMappingProvider, ProguardMappingFormat>, ProguardMappingFormat> {
    override fun getDownloadedFile(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<MojangMappingProvider, ProguardMappingFormat>
    ) = cacheDir(project).resolve("$version.proguard").toFile()

    override fun getURI(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<MojangMappingProvider, ProguardMappingFormat>
    ) = URI(PISTON_VERSIONS.getVersionMeta(version).downloads.serverMappings.url)

    override fun name() = "mojang"
}