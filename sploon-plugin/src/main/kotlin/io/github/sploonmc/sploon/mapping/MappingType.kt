package io.github.sploonmc.sploon.mapping

import io.github.sploonmc.sploon.mapping.provider.CustomMappingProvider
import io.github.sploonmc.sploon.mapping.provider.MappingProvider
import io.github.sploonmc.sploon.mapping.provider.MojangMappingProvider
import io.github.sploonmc.sploon.mapping.provider.NoopMappingProvider
import io.github.sploonmc.sploon.mapping.provider.SpigotMappingProvider
import net.fabricmc.mappingio.format.MappingFormat
import java.io.File

interface MappingType<P : MappingProvider<MappingType<P>>> {
    val format: MappingFormat
    val providerFactory: () -> P
    val reversed: Boolean

    data object Mojang : MappingType<MojangMappingProvider> {
        override val format = MappingFormat.PROGUARD_FILE
        override fun toString() = "mojang"
        override val providerFactory = { MojangMappingProvider }
        override val reversed = true
    }

    data object Spigot : MappingType<SpigotMappingProvider> {
        override val format = MappingFormat.CSRG_FILE
        override fun toString() = "spigot"
        override val providerFactory = { SpigotMappingProvider }
        override val reversed = false
    }

    // TODO
    data object Intermediary : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_FILE
        override fun toString() = "intermediary"
        override val providerFactory = { NoopMappingProvider }
        override val reversed = false
    }

    // TODO
    data object Yarn : MappingType<NoopMappingProvider> {
        override val format = MappingFormat.TINY_2_FILE
        override fun toString() = "yarn"
        override val providerFactory = { NoopMappingProvider }
        override val reversed = false
    }

    data class Custom(val mappingsFile: File, override val format: MappingFormat, override val reversed: Boolean) :
        MappingType<CustomMappingProvider> {
        override fun toString() = mappingsFile.nameWithoutExtension
        override val providerFactory = { CustomMappingProvider(mappingsFile) }
    }
}
