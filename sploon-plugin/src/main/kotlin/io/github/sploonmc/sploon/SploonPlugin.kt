package io.github.sploonmc.sploon

import io.github.sploonmc.sploon.ext.SploonDependenciesExt
import io.github.sploonmc.sploon.piston.getPistonVersions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Problems
import javax.inject.Inject

abstract class SploonPlugin @Inject constructor(val problems: Problems) : Plugin<Project> {
    override fun apply(project: Project) {
        val reporter: ProblemReporter = problems.forNamespace(SPLOON_NAME)

        project.dependencies.extensions.create("sploon", SploonDependenciesExt::class.java, project, reporter)
    }

    companion object {
        val PISTON_VERSIONS = getPistonVersions()
    }
}