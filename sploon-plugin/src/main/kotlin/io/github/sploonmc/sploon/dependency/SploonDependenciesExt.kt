package io.github.sploonmc.sploon.dependency

import io.github.sploonmc.sploon.SPLOON_NAME
import io.github.sploonmc.sploon.bundling.SploonBundling
import io.github.sploonmc.sploon.compileOnly
import io.github.sploonmc.sploon.mapping.MappingType
import io.github.sploonmc.sploon.mapping.format.MappingFormat
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Project
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Severity
import java.io.File

abstract class SploonDependenciesExt(
    private val project: Project,
    private val problemReporter: ProblemReporter,
) {
    /**
     * Adds the Spigot API dependency. This does not include CraftBukkit internals or Minecraft code (often referred to as NMS)
     * @param version The version of minecraft to download the Spigot API for.
     */
    fun spigot(version: String) {
        val mcVersion = MinecraftVersion.parse(version) ?: run {
            problemReporter.reportInvalidVersion(version)
            return
        }

        if (!MinecraftVersion.validateVersion(mcVersion)) {
            problemReporter.reportInvalidVersion(version)
            return
        }

        SploonDependencies.handleSpigot(project, mcVersion)
    }

    /**
     * Adds the Minecraft Server along with CraftBukkit internals and the Spigot API. Mojang-mapped.
     *
     * @param version The version of minecraft.
     */
    fun minecraft(version: String) = minecraft(version, MappingType.Mojang)

    /**
     * Adds the Minecraft Server along with CraftBukkit internals and the Spigot API.
     *
     * @param version The version of minecraft.
     * @param mapping The mappings to be used for internals. Defaults to Mojang.
     */
    fun <P : MappingProvider<MappingType<P, F>, F>, F : MappingFormat<F>> minecraft(version: String, mapping: MappingType<P, F>) {
        val mcVersion = MinecraftVersion.parse(version) ?: run {
            problemReporter.reportInvalidVersion(version)
            return
        }

        if (!MinecraftVersion.validateVersion(mcVersion)) {
            problemReporter.reportInvalidVersion(version)
            return
        }

        SploonDependencies.handleMinecraft(
            project,
            mcVersion,
            mapping
        )
    }

    fun pluginImplementation(dependencyNotation: Any) {
        val deps = SploonBundling.download(project, listOf(dependencyNotation))
        val jarFiles = deps.map { it.first }

        project.compileOnly(dependencyNotation)

        SploonBundling.apply(project, jarFiles.filter(File::isFile))
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