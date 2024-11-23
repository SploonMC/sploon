package io.github.sploonmc.sploon.mapping

import io.github.sploonmc.sploon.mapping.format.MappingFormat
import io.github.sploonmc.sploon.mapping.format.ProguardMappingFormat
import io.github.sploonmc.sploon.mapping.format.TinyV2MappingFormat
import io.github.sploonmc.sploon.mapping.provider.CustomMappingProvider
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.mapping.provider.MojangMappingProvider
import io.github.sploonmc.sploon.mapping.provider.NoopMappingProvider
import java.io.File
import kotlin.reflect.full.primaryConstructor

interface MappingType<P : MappingProvider<MappingType<P, F>, F>, F: MappingFormat<F>> {
    data object Mojang : MappingType<MojangMappingProvider, ProguardMappingFormat>

    // TODO
    data object Intermediary : MappingType<NoopMappingProvider, TinyV2MappingFormat>
    // TODO
    data object Yarn : MappingType<NoopMappingProvider, TinyV2MappingFormat>

    data class Custom<F : MappingFormat<F>>(val mappingsFile: File) : MappingType<CustomMappingProvider<F>, F>
}

inline fun <reified F : MappingFormat<F>> MappingType<*, F>.newMappingFormat(): F {
    return (F::class.primaryConstructor ?: error("class ${F::class.simpleName} has no constructor")).call()
}

inline fun <reified P : MappingProvider<MappingType<P, F>, F>, F : MappingFormat<F>> MappingType<P, F>.newProvider(): P {
    return (P::class.primaryConstructor ?: error("class ${P::class.simpleName} has no constructor")).call()
}