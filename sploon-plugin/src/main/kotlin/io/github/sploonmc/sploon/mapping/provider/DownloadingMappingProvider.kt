package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.downloadUri
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import java.io.File
import java.net.URI

interface DownloadingMappingProvider<T : MappingType<*>> : MappingProvider<T> {
    fun getDownloadedFile(
        project: Project,
        version: MinecraftVersion,
        type: T
    ): File

    fun getURI(
        project: Project,
        version: MinecraftVersion,
        type: T
    ): URI

    override fun getMappingFor(project: Project, version: MinecraftVersion, type: T): File {
        val file = getDownloadedFile(project, version, type)
        if (file.exists()) return file

        downloadUri(getURI(project, version, type), file.toPath())

        return file
    }
}