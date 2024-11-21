package io.github.sploonmc.sploon.bundling

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.sploonmc.sploon.SPLOON_BUNDLED_RESOURCES_DIRECTORY
import io.github.sploonmc.sploon.SPLOON_BUNDLE_DEPENDENCIES_TASK
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.Copy
import java.io.File

object SploonBundling {
    val bundledDependencies = mutableListOf<Pair<File, Dependency>>()

    fun download(project: Project, dependencies: List<Any>): List<Pair<File, Dependency>> {
        return dependencies.flatMap { dependencyNotation ->
            val dependency = project.dependencies.create(dependencyNotation)
            project.configurations.detachedConfiguration(dependency).resolve().map { it to dependency }
        }.also(bundledDependencies::addAll)
    }

    fun apply(project: Project, jarFiles: List<File>) {
        val taskExists = project.tasks.any { task -> task.name == SPLOON_BUNDLE_DEPENDENCIES_TASK }
        val buildDir = project.layout.buildDirectory.asFile.get()
        val configuration: (Copy) -> Unit = { task ->
            jarFiles.forEach { jar ->
                task.from(jar) { subtask ->
                    subtask.from(jar)
                    subtask.into(SPLOON_BUNDLED_RESOURCES_DIRECTORY)
                }
            }

            task.include("*.jar")
            task.into(File(buildDir, "resources/main"))
            task.doLast {
                println("Copied jar files to ${File(buildDir, "resources/main/$SPLOON_BUNDLED_RESOURCES_DIRECTORY")}")
            }
        }

        if (taskExists) {
            project.tasks.named(SPLOON_BUNDLE_DEPENDENCIES_TASK, Copy::class.java, configuration)
        } else {
            project.tasks.register(SPLOON_BUNDLE_DEPENDENCIES_TASK, Copy::class.java, configuration)
        }

        project.tasks.getByName("processResources").dependsOn(SPLOON_BUNDLE_DEPENDENCIES_TASK)
        project.tasks.getByName("assemble").dependsOn("processResources")
        project.tasks.getByName("shadowJar").dependsOn(SPLOON_BUNDLE_DEPENDENCIES_TASK)

        configureShadow(project, jarFiles)
    }

    fun configureShadow(project: Project, jarFiles: List<File>) {
        project.tasks.named("shadowJar", ShadowJar::class.java) { task ->
            task.dependsOn(SPLOON_BUNDLE_DEPENDENCIES_TASK)

            task.exclude { file ->
                val bundledDeps = bundledDependencies.map { it.first }
                val excluded = bundledDeps.any { it.name == file.name }
                excluded
            }

            jarFiles.forEach { jar ->
                task.from(jar) { copy ->
                    copy.from(jar)
                    copy.into(SPLOON_BUNDLED_RESOURCES_DIRECTORY)
                }
            }

            task.doLast {
                File(SPLOON_BUNDLED_RESOURCES_DIRECTORY).listFiles()?.forEach { file ->
                    println(file.name)
                }
            }
        }
    }
}
