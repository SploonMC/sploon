package io.github.sploonmc.sploon.ext

import io.github.sploonmc.sploon.minecraft.MappingType
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Severity
import java.io.File

abstract class SploonDependenciesExt(
    private val dependencies: DependencyHandler,
    private val problemReporter: ProblemReporter,
) {
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
        if (mcVersion !in MinecraftVersion.VERSIONS) {
            problemReporter.reportInvalidVersion(string)
            return
        }

        println("adding spigot for mc $mcVersion")
    }

    fun minecraft(version: Any, mapping: MappingType?) {
        val string = version.toString()
        val mcVersion = MinecraftVersion.fromString(string) ?: run {
            problemReporter.reportInvalidVersion(string)
            return
        }
        if (mcVersion !in MinecraftVersion.VERSIONS) {
            problemReporter.reportInvalidVersion(string)
            return
        }

        val finalMapping = mapping ?: MappingType.Mojang
        println("adding mc internals and spigot for mc $mcVersion with mappings ${finalMapping::class.simpleName}")
    }

    private fun ProblemReporter.reportInvalidVersion(input: String) {
        throwing { spec ->
            spec
                .id("sploon", "Invalid Minecraft version")
                .severity(Severity.ERROR)
                .solution("Please enter a valid Minecraft version, such as 1.21.3. Has Spigot updated yet?")
                .withException(MinecraftVersion.InvalidMinecraftVersion(input))
        }
    }
}