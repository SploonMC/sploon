package io.github.sploonmc.sploon.minecraft

import java.io.File

sealed interface MappingType {
    data object Mojang : MappingType
    data object Intermediary : MappingType
    data object Yarn : MappingType
    data object Obf : MappingType
    data class Custom(val mappingsFile: File) : MappingType
}