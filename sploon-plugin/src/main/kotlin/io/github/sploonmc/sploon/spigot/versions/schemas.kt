package io.github.sploonmc.sploon.spigot.versions

import io.github.sploonmc.sploon.patcher.CommitHashes
import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val name: String,
    val description: String,
    val refs: CommitHashes,
    val toolsVersion: Int,
    val javaVersions: List<Int>
)
