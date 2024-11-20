package io.github.sploonmc.sploon.bundling

import io.github.sploonmc.sploon.SPLOON_BUNDLED_RESOURCES_DIRECTORY
import io.github.sploonmc.sploon.SPLOON_BUNDLE_DEPENDENCIES_TASK
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.Copy
import java.io.File

object SploonBundling {
    fun download(project: Project, dependencies: List<Any>): List<Pair<File, Dependency>> {
        return dependencies.flatMap { dependencyNotation ->
            val dependency = project.dependencies.create(dependencyNotation)
            project.configurations.detachedConfiguration(dependency).resolve().map { it to dependency }
        }
    }

    fun apply(project: Project, jarFiles: List<File>) {
        val taskExists = project.tasks.any { task -> task.name == SPLOON_BUNDLE_DEPENDENCIES_TASK }
        val buildDir = project.layout.buildDirectory.asFile.get()

        val configuration: (Copy) -> Unit = { task ->
            jarFiles.forEach { jar ->
                task.from(jar.parent)
            }

            task.include("*.jar")
            task.into(File(buildDir, "resources/main/META-INF/$SPLOON_BUNDLED_RESOURCES_DIRECTORY"))
        }

        if (taskExists) {
            project.tasks.named(SPLOON_BUNDLE_DEPENDENCIES_TASK, Copy::class.java, configuration)
        } else {
            project.tasks.register(SPLOON_BUNDLE_DEPENDENCIES_TASK, Copy::class.java, configuration)
        }

        project.tasks.getByName("processResources").dependsOn(SPLOON_BUNDLE_DEPENDENCIES_TASK)
        project.tasks.getByName("assemble").dependsOn("processResources")
    }
}