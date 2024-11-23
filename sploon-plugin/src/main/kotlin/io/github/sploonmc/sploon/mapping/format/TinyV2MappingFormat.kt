package io.github.sploonmc.sploon.mapping.format

class TinyV2MappingFormat : TinyMappingFormat<TinyV2MappingFormat> {
    override fun mappingIo() = MappingIoFormat.TINY_2_FILE
}