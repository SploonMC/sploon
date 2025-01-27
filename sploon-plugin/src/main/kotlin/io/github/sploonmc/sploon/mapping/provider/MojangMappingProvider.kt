package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.JSON
import io.github.sploonmc.sploon.SploonPlugin.Companion.PISTON_VERSIONS
import io.github.sploonmc.sploon.downloadUri
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.piston.getVersionMeta
import io.github.sploonmc.sploon.spigot.builddata.BuildData
import io.github.sploonmc.sploon.spigot.builddata.BuildDataInfo
import io.github.sploonmc.sploon.spigot.versions.getVersionData
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingUtil
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingDstNsReorder
import net.fabricmc.mappingio.adapter.MappingNsCompleter
import net.fabricmc.mappingio.adapter.MappingNsRenamer
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import org.gradle.api.Project
import java.io.File
import java.net.URI
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.readText

object MojangMappingProvider: DownloadingMappingProvider<MappingType<MojangMappingProvider>> {
    override fun getDownloadedFile(
        project: Project, version: MinecraftVersion, type: MappingType<MojangMappingProvider>
    ) = cacheDir(project).resolve("$version.txt").toFile()

    override fun getURI(
        project: Project, version: MinecraftVersion, type: MappingType<MojangMappingProvider>
    ) = URI(PISTON_VERSIONS.getVersionMeta(version).downloads.serverMappings.url)

    override fun name() = "mojang"

    @OptIn(ExperimentalPathApi::class)
    override fun getMappingFor(project: Project, version: MinecraftVersion, type: MappingType<MojangMappingProvider>): File {
        val outputFile = MojangMappingProvider.cacheDir(project).resolve("$version.${type.format.fileExt}")
        if (outputFile.exists()) return outputFile.toFile()

        val buildDataDir = MojangMappingProvider.cacheDir(project).resolve("build-data/$version")
        if (buildDataDir.exists()) buildDataDir.deleteRecursively()
        buildDataDir.createParentDirectories()

        val buildData = BuildData(buildDataDir)
        val commit = getVersionData(version).refs.buildData

        val commitDir = buildData.extractCommit(commit)
        val buildDataInfo = JSON.decodeFromString<BuildDataInfo>(commitDir.resolve("info.json").readText())

        val mappingsDir = commitDir.resolve("mappings")

        val mojangMappings = cacheDir(project).resolve("$version.txt")
        if (mojangMappings.notExists()) downloadUri(URI(buildDataInfo.mappingsUrl), mojangMappings)

        val fallbackSrcNs = MappingUtil.NS_SOURCE_FALLBACK
        val fallbackDstNs = MappingUtil.NS_TARGET_FALLBACK
        val obfNs = "obfuscated"
        val mojmapNs = "mojang"
        val spigotNs = "spigot"
        val tree = MemoryMappingTree()

        MappingReader.read(
            mappingsDir.resolve(buildDataInfo.classMappings), MappingNsRenamer(
                tree, mapOf(fallbackSrcNs to obfNs, fallbackDstNs to mojmapNs)
            )
        )
        MappingReader.read(
            mojangMappings, MappingNsRenamer(
                tree, mapOf(fallbackSrcNs to obfNs, fallbackDstNs to spigotNs)
            )
        )


        tree.accept(
            MappingSourceNsSwitch(
                MappingDstNsReorder(
                    MappingWriter.create(outputFile, MappingFormat.TINY_2_FILE), spigotNs
                ), mojmapNs
            )
        )

        return outputFile.toFile()
    }
}