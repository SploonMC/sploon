package io.github.sploonmc.sploon.mapping.provider

import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import java.io.File
import kotlin.io.path.Path

interface MappingProvider<T : MappingType<*>> {
    fun getMappingFor(project: Project, version: MinecraftVersion, type: T): File
    fun name(): String

    fun cacheDir(project: Project) = Path(project.gradle.gradleUserHomeDir.path, "caches/sploon/mappings/${name()}")
}