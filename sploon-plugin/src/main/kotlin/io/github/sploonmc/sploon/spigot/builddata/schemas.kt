package io.github.sploonmc.sploon.spigot.builddata

import kotlinx.serialization.Serializable

@Serializable
data class BuildDataInfo(
    val minecraftVersion: String,
    val spigotVersion: String,
    val minecraftHash: String,
    val serverUrl: String,
    val mappingsUrl: String,
    val accessTransforms: String,
    val classMappings: String,
    val memberMappings: String? = null,
    val packageMappings: String,
    val classMapCommand: String,
    val memberMapCommand: String,
    val finalMapCommand: String,
    val decompileCommand: String,
    val toolsVersion: Int
)