package io.github.sploonmc.sploon

import io.github.sploonmc.sploon.ext.SploonDependenciesExt
import io.github.sploonmc.sploon.minecraft.MinecraftVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Problems
import javax.inject.Inject

abstract class SploonPlugin @Inject constructor(val problems: Problems) : Plugin<Project> {
    override fun apply(target: Project) {
        val reporter: ProblemReporter = problems.forNamespace(SPLOON_NAME)

        target.dependencies.extensions.create(SPLOON_NAME, SploonDependenciesExt::class.java, target.dependencies, reporter)
    }
}