package io.github.sploonmc.sploon.minecraft

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
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

    class InvalidMinecraftVersion(input: String) : RuntimeException("Invalid Minecraft version: $input - Has Spigot updated yet?")

    companion object {
        fun fromString(input: String) = runCatching {
            val numbers = input.split(".").map(String::toInt)
            MinecraftVersion(numbers.getOrNull(0) ?: 0, numbers.getOrNull(1) ?: 0, numbers.getOrNull(2) ?: 0)
        }.getOrNull()

        private fun getVersions(): List<MinecraftVersion> {
            val uri = URI("https://hub.spigotmc.org/versions/")
            val versionPattern = Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+|[0-9]+\\.[0-9]+)")

            val response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            ).body()

            val matcher = versionPattern.matcher(response)

            return buildList {
                while (matcher.find()) {
                    fromString(matcher.group())?.let(::add)
                }
            }
        }

        val VERSIONS = getVersions().toSet()
    }
}