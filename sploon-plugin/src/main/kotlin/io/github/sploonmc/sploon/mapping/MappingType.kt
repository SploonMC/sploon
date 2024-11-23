package io.github.sploonmc.sploon.mapping

import io.github.sploonmc.sploon.mapping.provider.CustomMappingProvider
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.mapping.provider.MojangMappingProvider
import io.github.sploonmc.sploon.mapping.provider.NoopMappingProvider
import net.fabricmc.mappingio.format.MappingFormat
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

interface MappingType<P : MappingProvider<MappingType<P>>> {
    val format: MappingFormat
    val providerKlass: KClass<P>

    data object Mojang : MappingType<MojangMappingProvider> {
        override val format = MappingFormat.PROGUARD_FILE
        override fun toString() = "mojang"
        override val providerKlass = MojangMappingProvider::class
    }

    // TODO
    data object Intermediary : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_2_FILE
        override fun toString() = "intermediary"
        override val providerKlass = NoopMappingProvider::class
    }

    // TODO
    data object Yarn : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_2_FILE
        override fun toString() = "yarn"
        override val providerKlass = NoopMappingProvider::class
    }

    data class Custom(val mappingsFile: File, override val format: MappingFormat) : MappingType<CustomMappingProvider> {
        override fun toString() = mappingsFile.nameWithoutExtension
        override val providerKlass = CustomMappingProvider::class
    }
}

fun <P : MappingProvider<MappingType<P>>> MappingType<P>.newProvider(klass: KClass<P>): P {
    return (klass.primaryConstructor ?: error("class ${klass.simpleName} has no constructor")).call()
}
