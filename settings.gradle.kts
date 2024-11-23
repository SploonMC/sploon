pluginManagement {
    includeBuild("sploon-plugin")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
}

rootProject.name = "sploon"
include("test")