plugins {
    kotlin("jvm") version "2.0.20" apply false
}

val groupProp = property("group") as String
val versionProp = property("version") as String

allprojects {
    group = groupProp
    version = versionProp
}