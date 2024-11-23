package io.github.sploonmc.sploon.mapping.format

class ProguardMappingFormat : MappingFormat<ProguardMappingFormat> {
    override fun mappingIo() = MappingIoFormat.PROGUARD_FILE
}