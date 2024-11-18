package io.github.sploonmc.sploon.piston

import io.github.sploonmc.sploon.getUriJson
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import java.net.URI

private const val PISTON_META_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"

fun getPistonVersions() = getUriJson<PistonVersionsResponse>(URI(PISTON_META_URL))

fun PistonVersionsResponse.getVersionMeta(version: MinecraftVersion) = getUriJson<PistonVersionMeta>(
    URI(
        versions.find { pistonVersion -> pistonVersion.id == version.toString() }?.url
            ?: throw MinecraftVersion.InvalidMinecraftVersionException(version.toString())
    )
)