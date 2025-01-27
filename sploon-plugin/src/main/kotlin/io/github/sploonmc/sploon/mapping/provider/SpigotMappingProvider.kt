package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.JSON
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.spigot.builddata.BuildData
import io.github.sploonmc.sploon.spigot.builddata.BuildDataInfo
import io.github.sploonmc.sploon.spigot.versions.getVersionData
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import org.gradle.api.Project
import java.io.File
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.readBytes
import kotlin.io.path.readText

object SpigotMappingProvider : MappingProvider<MappingType<SpigotMappingProvider>> {
    @OptIn(ExperimentalPathApi::class)
    override fun getMappingFor(
        project: Project,
        version: MinecraftVersion,
        type: MappingType<SpigotMappingProvider>
    ): File {
        val outputFile = cacheDir(project).resolve("$version.${type.format.fileExt}")
        if (outputFile.exists()) return outputFile.toFile()

        val buildDataDir = cacheDir(project).resolve("build-data/$version")
        if (buildDataDir.exists()) buildDataDir.deleteRecursively()
        buildDataDir.createParentDirectories()

        val buildData = BuildData(buildDataDir)
        val commit = getVersionData(version).refs.buildData

        val commitDir = buildData.extractCommit(commit)
        val buildDataInfo = JSON.decodeFromString<BuildDataInfo>(commitDir.resolve("info.json").readText())

        val mappingsDir = commitDir.resolve("mappings")
        val mappingFiles = listOfNotNull(buildDataInfo.packageMappings, buildDataInfo.memberMappings, buildDataInfo.classMappings)
            .map(mappingsDir::resolve)

        val mappingTree = MemoryMappingTree()
        mappingFiles.forEach { file ->
            if (file.readBytes().isEmpty()) return@forEach // File empty for some reason, we'll ignore it
            MappingReader.read(file, mappingTree)
        }
        mappingTree.accept(MappingWriter.create(outputFile, type.format))

        return outputFile.toFile()
    }

    override fun name() = "spigot"
}
