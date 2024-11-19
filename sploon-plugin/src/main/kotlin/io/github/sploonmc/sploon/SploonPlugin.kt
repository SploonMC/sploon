package io.github.sploonmc.sploon

import io.github.sploonmc.sploon.dependency.SploonDependenciesExt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Problems
import javax.inject.Inject

abstract class SploonPlugin @Inject constructor(val problems: Problems) : Plugin<Project> {
    override fun apply(project: Project) {
        val reporter: ProblemReporter = problems.forNamespace(SPLOON_NAME)

        project.dependencies.extensions.create(SPLOON_NAME, SploonDependenciesExt::class.java, project, reporter)

        project.repos(MAVEN_CENTRAL_REPO_URL, SPIGOT_REPO_URL, SONATYPE_OSS_SNAPSHOTS_URL, SONATYPE_OSS_CENTRAL_URL, MINECRAFT_REPO_URL)
    }
}