package io.github.sploonmc.sploon.spigot.versions

import io.github.sploonmc.sploon.getUriJson
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import java.net.URI

private const val SPIGOT_VERSIONS_URL = "https://hub.spigotmc.org/versions/"

fun getVersionData(version: MinecraftVersion) = getUriJson<Version>(
    URI(
        SPIGOT_VERSIONS_URL.plus("$version.json")
    )
)