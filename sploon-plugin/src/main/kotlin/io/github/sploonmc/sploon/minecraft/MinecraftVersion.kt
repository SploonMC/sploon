package io.github.sploonmc.sploon.minecraft

import io.github.sploonmc.sploon.getUri
import io.github.sploonmc.sploon.patcher.Patcher
import java.net.URI
import java.util.regex.Pattern

data class MinecraftVersion(val major: Int, val minor: Int, val rev: Int) {
    override fun toString() = buildString {
        if (major != 0) append("$major")
        if (minor != 0) append(".$minor")
        if (rev != 0) append(".$rev")
    }

    val javaVersionRequired: Int
        get() {
            if (minor < 17) return 8
            if (minor == 17) return 16
            if (minor < 20 && rev < 5) return 17
            return 21
        }

    class InvalidMinecraftVersionException(input: String) : RuntimeException("Invalid Minecraft version: $input - Has Spigot updated yet?")

    companion object {
        fun fromString(input: String) = runCatching {
            val numbers = input.split(".").map(String::toInt)
            MinecraftVersion(numbers.getOrNull(0) ?: 0, numbers.getOrNull(1) ?: 0, numbers.getOrNull(2) ?: 0)
        }.getOrNull()

        fun validateVersion(version: MinecraftVersion) = runCatching { getUri(URI("${Patcher.PATCH_REPO_BASE_URL}/$version.json")) }.isSuccess
    }
}