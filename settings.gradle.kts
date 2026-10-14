pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version providers.gradleProperty("kotlin_plugin_version").get()
        id("net.fabricmc.fabric-loom") version providers.gradleProperty("fabric_loom_version").get()
    }

    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
