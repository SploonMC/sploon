package io.github.sploonmc.sploon.piston

import kotlinx.serialization.Serializable

@Serializable
data class PistonVersion(
    val id: String,
    val url: String,
)

@Serializable
data class PistonVersionsResponse(
    val versions: List<PistonVersion>
)

@Serializable
data class PistonVersionDownload(
    val url: String
)

@Serializable
data class PistonVersionDownloads(
    val server: PistonVersionDownload
)

@Serializable
data class PistonVersionMeta(
    val downloads: PistonVersionDownloads
)