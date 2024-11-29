package io.github.sploonmc.sploon.mapping

import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.io.path.exists

class Remapper<P : MappingProvider<MappingType<P>>>(
    val cacheDir: Path,
    val input: Path,
    val output: Path,
    val inputMappingType: MappingType<P>,
    val inputMapping: Path,
    val minecraftVersion: MinecraftVersion,
    val reverse: Boolean = false
) {
    val tinyMapped = IntoTinyRemapper.remap(inputMappingType.format, inputMapping, cacheDir.resolve("tiny-remapped/$minecraftVersion.tiny"))
    val mappingOrder = if (reverse) listOf("source", "target") else listOf("target", "source")
    val remapper = TinyRemapper.newRemapper()
        .withMappings(TinyUtils.createTinyMappingProvider(tinyMapped, mappingOrder[0], mappingOrder[1]))
        .withMappings { out -> JSR_TO_JETBRAINS.forEach(out::acceptClass) }
        .renameInvalidLocals(true)
        .invalidLvNamePattern(MC_LV_PATTERN)
        .inferNameFromSameLvIndex(true)
        .build()

    fun remap() {
        if (output.exists()) return

        println("Remapping...")
        OutputConsumerPath.Builder(output).build().use { consumerPath ->
            consumerPath.addNonClassFiles(input)
            remapper.readInputs(input)
            remapper.apply(consumerPath)
        }

        remapper.finish()
        throw RuntimeException("Remapped!")
    }

    companion object {
        val JSR_TO_JETBRAINS = buildMap {
            this["javax/annotation/Nullable"] = "org/jetbrains/annotations/Nullable"
            this["javax/annotation/Nonnull"] = "org/jetbrains/annotations/NotNull"
            this["javax/annotation/concurrent/Immutable"] = "org/jetbrains/annotations/Unmodifiable"
        }
        val MC_LV_PATTERN = Pattern.compile("\\$\\$\\d+")
    }
}
