package io.github.sploonmc.sploon.spigot.builddata

import com.github.syari.kgit.KGit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.TreeFilter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.outputStream

class BuildData(
    val path: Path,
    val vcsLink: String = "https://hub.spigotmc.org/stash/scm/spigot/builddata.git"
) : AutoCloseable {
    val git: KGit = KGit.cloneRepository {
        setURI(vcsLink)
        setDirectory(path.toFile())
    }

    fun extractCommit(commit: String): Path {
        val repo = git.repository

        return RevWalk(repo).use { revWalk ->
            val commit = revWalk.parseCommit(repo.resolve(commit))
            val tempDir = Files.createTempDirectory("sploon-build-data-${commit.name}")
            //tempDir.toFile().deleteOnExit()

            TreeWalk(repo).apply {
                addTree(commit.tree)
                isRecursive = true
                filter = TreeFilter.ALL
            }.use { treeWalk ->
                while (treeWalk.next()) {
                    val objectId = treeWalk.getObjectId(0)
                    val entryPath = treeWalk.pathString
                    val outputPath = tempDir.resolve(entryPath)

                    outputPath.createParentDirectories()

                    repo.open(objectId).openStream().transferTo(outputPath.outputStream())
                }
            }

            tempDir
        }
    }

    override fun close() {
        git.close()
    }
}
