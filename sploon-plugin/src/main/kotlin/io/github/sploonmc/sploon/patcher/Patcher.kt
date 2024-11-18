package io.github.sploonmc.sploon.patcher

import io.github.sploonmc.sploon.JAR_VERSIONS_PATH
import io.github.sploonmc.sploon.JSON
import io.github.sploonmc.sploon.SploonPlugin.Companion.PISTON_VERSIONS
import io.github.sploonmc.sploon.downloadUri
import io.github.sploonmc.sploon.extractJar
import io.github.sploonmc.sploon.getUri
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import io.github.sploonmc.sploon.piston.getVersionMeta
import io.github.sploonmc.sploon.sha1
import io.sigpipe.jbsdiff.Patch
import org.gradle.api.invocation.Gradle
import java.net.URI
import java.util.jar.JarFile
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText

class Patcher(val version: MinecraftVersion, gradle: Gradle) {
    val cacheDir = Path(gradle.gradleUserHomeDir.path, "caches/sploon")
    val versionMeta = PISTON_VERSIONS.getVersionMeta(version)
    val patchedMeta = JSON.decodeFromString<PatchedVersionMeta>(getUri(URI("$PATCH_REPO_BASE_URL/$version.json")))
    val vanillaJar = cacheDir.resolve("vanilla/$version.jar")
    val patch = cacheDir.resolve("patches/$version.patch")
    val spigotJar = cacheDir.resolve("spigot/$version.jar")
    val vanillaHashMatches = if (vanillaJar.exists()) vanillaJar.sha1() == patchedMeta.vanillaJarHash else false
    val patchHashMatches = if (patch.exists()) patch.sha1() == patchedMeta.patchHash else false
    val spigotHashMatches = if (spigotJar.exists()) spigotJar.sha1() == patchedMeta.patchedJarHash else false
    val isCached = vanillaHashMatches && patchHashMatches && spigotHashMatches

    fun download() {
        if (isCached) return

        vanillaJar.toFile().parentFile.mkdirs()
        patch.toFile().parentFile.mkdirs()
        spigotJar.toFile().parentFile.mkdirs()

        if (!vanillaHashMatches) downloadUri(URI(patchedMeta.vanillaDownloadUrl), vanillaJar)
        if (!patchHashMatches) downloadUri(URI("$PATCH_REPO_BASE_URL/$version.patch"), patch)
    }

    @OptIn(ExperimentalPathApi::class)
    fun patch() {
        val vanillaJarArchive = JarFile(vanillaJar.toFile())
        val needsExtraction = vanillaJarArchive.getJarEntry(JAR_VERSIONS_PATH) != null
        val extractedVanillaJar = if (needsExtraction) {
            println("Vanilla jar needs extracting")
            val vanillaJarExtractionPath = cacheDir.resolve("vanilla/extracted/$version")
            if (vanillaJarExtractionPath.exists()) {
                vanillaJarExtractionPath.deleteRecursively()
            }

            vanillaJarExtractionPath.toFile().mkdirs()

            extractJar(vanillaJar, vanillaJarExtractionPath)

            val extractionPath = vanillaJarExtractionPath.resolve(JAR_VERSIONS_PATH)
            val versionsFilePath = vanillaJarExtractionPath.resolve("META-INF/versions.list")

            val fileContent = runCatching(versionsFilePath::readText).getOrNull()

            if (fileContent == null) {
                println("Failed reading versions.list. Using $vanillaJar instead.")
                vanillaJar
            } else {
                extractionPath.resolve(fileContent.split("\t")[2])
            }
        } else {
            println("Vanilla jar does not need extracting")
            vanillaJar
        }

        val vanillaBytes = extractedVanillaJar.readBytes()
        val patchBytes = patch.readBytes()
        spigotJar.outputStream().use { stream -> Patch.patch(vanillaBytes, patchBytes, stream) }
    }

    companion object {
        const val PATCH_REPO_BASE_URL = "https://raw.githubusercontent.com/SploonMC/patches/refs/heads/master"
    }
}