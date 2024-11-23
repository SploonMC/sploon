package io.github.sploonmc.sploon.mapping.format

typealias MappingIoFormat = net.fabricmc.mappingio.format.MappingFormat

interface MappingFormat<T : MappingFormat<T>> {
    fun mappingIo(): MappingIoFormat
}