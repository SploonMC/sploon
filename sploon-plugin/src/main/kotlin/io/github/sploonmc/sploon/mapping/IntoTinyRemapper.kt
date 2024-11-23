package io.github.sploonmc.sploon.mapping

import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import java.nio.file.Path
import kotlin.io.path.createDirectories

object IntoTinyRemapper {
    fun remap(inputFormat: MappingFormat, input: Path, output: Path): Path {
        if (inputFormat == MappingFormat.TINY_2_FILE) return output
        val tree = MemoryMappingTree()

        MappingReader.read(input, inputFormat, tree)

        output.parent.createDirectories()

        tree.accept(MappingWriter.create(output, MappingFormat.TINY_2_FILE))

        return output
    }
}