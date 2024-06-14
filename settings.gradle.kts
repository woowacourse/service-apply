pluginManagement {
    val kotlin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version
        kotlin("plugin.jpa") version kotlin_version
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    }
}
rootProject.name = "apply"
