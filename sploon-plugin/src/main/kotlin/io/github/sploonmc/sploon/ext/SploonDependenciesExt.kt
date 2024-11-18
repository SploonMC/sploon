package io.github.sploonmc.sploon.ext

import io.github.sploonmc.sploon.SPLOON_NAME
import io.github.sploonmc.sploon.minecraft.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.patcher.Patcher
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.artifacts.dependencies.DefaultFileCollectionDependency
import org.gradle.api.invocation.Gradle
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Severity

abstract class SploonDependenciesExt(
    private val project: Project,
    private val problemReporter: ProblemReporter,
) {
    val dependencies = project.dependencies
    val gradle = project.gradle

    /**
     * Adds the Spigot API dependency. This does not include CraftBukkit internals or Minecraft code (often referred to as NMS)
     * @param version The version of minecraft to download the Spigot API for.
     */
    fun spigot(version: Any) {
        val string = version.toString()
        val mcVersion = MinecraftVersion.fromString(string) ?: run {
            problemReporter.reportInvalidVersion(string)
            return
        }

        if (!MinecraftVersion.validateVersion(mcVersion)) {
            problemReporter.reportInvalidVersion(string)
            return
        }
    }

    fun minecraft(version: Any, mapping: MappingType = MappingType.Mojang) {
        val string = version.toString()
        val mcVersion = MinecraftVersion.fromString(string) ?: run {
            problemReporter.reportInvalidVersion(string)
            return
        }

        if (!MinecraftVersion.validateVersion(mcVersion)) {
            problemReporter.reportInvalidVersion(string)
            return
        }

        val patcher = Patcher(mcVersion, gradle)
        patcher.download()
        if (!patcher.isCached) patcher.patch()

        dependencies.add("compileOnly", dependencies.create(project.files(patcher.spigotJar.toFile())))
    }

    private fun ProblemReporter.reportInvalidVersion(input: String) {
        throwing { spec ->
            spec
                .id(SPLOON_NAME, "Invalid Minecraft version")
                .severity(Severity.ERROR)
                .contextualLabel("invalid minecraft version")
                .solution("Please enter a valid Minecraft version, such as 1.21.3. Has Spigot updated yet?")
                .withException(MinecraftVersion.InvalidMinecraftVersionException(input))
                .details("The given Minecraft version is invalid and could not be found on Spigot.")
        }
    }
}