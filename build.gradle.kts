import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("net.fabricmc.fabric-loom")
    id("maven-publish")
    id("idea")
}

idea {
    module {
        isDownloadSources = true
    }
}

val targetJavaVersion = project.property("java_version").toString().toInt()
version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
        freeCompilerArgs.add("-Xnullability-annotations=@org.jspecify.annotations:warn")
    }
}

loom {
    accessWidenerPath = file("src/main/resources/yabr.classtweaker")

    runs {
        getByName("client") {
            jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
        }
        getByName("server") {
            jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    implementation(include("net.kyori:adventure-platform-fabric:${project.property("adventure_platform_fabric_version")}")!!)
    implementation("org.popcraft:chunky-fabric:${project.property("chunky_version")}")
    compileOnly("org.popcraft:chunky-common:${project.property("chunky_version")}")

    api("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("minecraft_version", project.property("minecraft_version"))
        inputs.property("loader_version", project.property("loader_version"))
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_version" to project.property("minecraft_version")!!,
                "loader_version" to project.property("loader_version")!!,
                "kotlin_loader_version" to project.property("kotlin_loader_version")!!,
            )
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
    }
}
