package io.github.sploonmc.sploon.project

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import io.github.sploonmc.sploon.SHADOW_PLUGIN_ID
import org.gradle.api.Project

object SploonProjectSetup {
    fun applyShadow(project: Project) {
        val isApplied = project.pluginManager.findPlugin(SHADOW_PLUGIN_ID) != null

        if (isApplied) return

        project.plugins.apply(ShadowPlugin::class.java)

        project.tasks.findByPath("assemble")?.dependsOn("shadowJar")
    }
}