package io.github.sploonmc.sploon.mapping

import io.github.sploonmc.sploon.mapping.provider.CustomMappingProvider
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.mapping.provider.MojangMappingProvider
import io.github.sploonmc.sploon.mapping.provider.NoopMappingProvider
import net.fabricmc.mappingio.format.MappingFormat
import java.io.File
import kotlin.reflect.full.primaryConstructor

interface MappingType<P : MappingProvider<MappingType<P>>> {
    val format: MappingFormat

    data object Mojang : MappingType<MojangMappingProvider> {
        override val format = MappingFormat.PROGUARD_FILE
    }

    // TODO
    data object Intermediary : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_2_FILE
    }

    // TODO
    data object Yarn : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_2_FILE
    }

    data class Custom(val mappingsFile: File, override val format: MappingFormat) : MappingType<CustomMappingProvider>
}

inline fun <reified P : MappingProvider<MappingType<P>>> MappingType<P>.newProvider(): P {
    return (P::class.primaryConstructor ?: error("class ${P::class.simpleName} has no constructor")).call()
}