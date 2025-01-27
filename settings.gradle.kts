pluginManagement {
    includeBuild("sploon-plugin")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://repo.epicebic.xyz/public")
    }
}

rootProject.name = "sploon"
include("test")