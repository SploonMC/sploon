package io.github.sploonmc.sploon

import kotlinx.serialization.json.Json
import org.gradle.api.Project
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.security.MessageDigest
import java.util.zip.ZipInputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists
import kotlin.io.path.readBytes

private val HTTP_CLIENT = HttpClient.newHttpClient()
val JSON = Json {
    ignoreUnknownKeys = true
}

fun downloadUri(uri: URI, output: Path) {
    val response = HTTP_CLIENT.send(
        HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build(),
        HttpResponse.BodyHandlers.ofFile(output)
    )

    response.body()
}

fun getUri(uri: URI): String {
    val response = HTTP_CLIENT.send(
        HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build(),
        HttpResponse.BodyHandlers.ofString()
    )

    return response.body()
}

inline fun <reified T> getUriJson(uri: URI): T = JSON.decodeFromString(getUri(uri))

fun Path.sha1() = readBytes()
    .inputStream()
    .use {
        MessageDigest.getInstance("SHA-1").digest(it.readBytes())
    }.joinToString("") { "%02x".format(it) }

fun Project.repos(vararg uris: String) {
    repositories.apply {
        uris.forEach { uri ->
            maven { repo ->
                repo.url = URI(uri)
            }
        }
    }
}

fun Project.compileOnly(dependencyNotation: Any) {
    dependencies.add("compileOnly", dependencies.create(dependencyNotation))
}

fun extractJar(jarFile: Path, outputDirectory: Path) {
    if (outputDirectory.notExists()) {
        outputDirectory.createDirectories()
    }

    FileInputStream(jarFile.toFile()).use { fileStream ->
        ZipInputStream(fileStream).use { zipStream ->
            var entry = zipStream.nextEntry
            while (entry != null) {
                val outputPath = outputDirectory.resolve(entry.name)

                if (entry.isDirectory) {
                    outputPath.createDirectories()
                } else {
                    outputPath.createParentDirectories()

                    FileOutputStream(outputPath.toFile()).use { fileOutStream ->
                        zipStream.copyTo(fileOutStream)
                    }
                }

                entry = zipStream.nextEntry
            }
        }
    }
}

