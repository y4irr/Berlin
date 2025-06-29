import dev.y4irr.repository.configureNexusPublishing
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.StringWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val currentDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

group = "net.cyruspvp.hub"
version = currentDate

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.maven.apache.org/maven2")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://libraries.minecraft.net/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.lunarclient.dev")
    maven("https://repo.ajg0702.us/releases")
}

dependencies {
    // Cambié compileOnly a implementation para que PacketEvents se incluya en el JAR final
    implementation("com.github.retrooper.packetevents:spigot:2.2.1") {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "org.jetbrains", module = "annotations")
    }

    // Dependencias locales
    compileOnly(files("libs/spigots/1.8.8.jar"))
    compileOnly(files("libs/spigots/1.16.jar"))
    compileOnly(files("libs/spigots/1.7.10.jar"))
    compileOnly(files("libs/spigots/1.17.jar"))
    compileOnly(files("libs/client/CheatBreakerAPI.jar"))
    compileOnly(files("libs/ViaVersion.jar"))
    compileOnly(files("libs/ViaVersionLatest.jar"))
    compileOnly(files("libs/ProtocolSupport.jar"))

    compileOnly("com.lunarclient:apollo-api:1.1.5")
    compileOnly("com.lunarclient:apollo-extra-adventure4:1.1.6")
    compileOnly("us.ajg0702.queue.api:api:2.0.7")

    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT") {
        exclude(group = "com.mojang", module = "authlib")
        exclude(group = "com.mojang", module = "datafixerupper")
        exclude(group = "com.mojang", module = "logging")
    }

    compileOnly("it.unimi.dsi:fastutil:8.5.12")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly(fileTree("libs/ranks") { include("VolcanoAPI.jar", "AquaCore-API.jar", "HeliumAPI-1.0-SNAPSHOT.jar") })
    compileOnly(fileTree("libs/bans") { include("LiteBansAPI.jar") })
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.github.MylesIsCool:ViaVersion:3.2.1")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    implementation("org.mongodb:mongo-java-driver:3.12.11")
    implementation("redis.clients:jedis:2.9.0")
    implementation("com.google.code.gson:gson:2.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.json:json:20230227")
    compileOnly("de.tr7zw:item-nbt-api:2.11.3")
    implementation("com.github.cryptomorin:XSeries:9.9.0") // Cambié compileOnly a implementation para incluirlo en el JAR final
    compileOnly("fr.mrmicky:FastParticles:2.0.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    archiveBaseName.set("Berlin")
    archiveVersion.set("${project.version}")

    doFirst {
        val writer = StringWriter()
        val pluginData = mapOf(
            "main" to "net.cyruspvp.hub.Berlin",
            "name" to project.name,
            "version" to project.version,
            "authors" to listOf("Astro Operations", "Comunidad", "Yair Soto"),
            "api-version" to "1.13",
            "depend" to listOf("PlaceholderAPI", "ProtocolLib"),
            "softdepend" to listOf("Kup", "Volcano", "CheatBreakerAPI", "Apollo-Bukkit")
        )

        pluginData.toYaml(writer)

        val pluginYamlFile = file("$buildDir/generated/plugin.yml")
        pluginYamlFile.parentFile.mkdirs()
        pluginYamlFile.writeText(writer.toString())
    }

    from("$buildDir/generated") {
        include("plugin.yml")
    }
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.build {
    finalizedBy("publishToMavenLocal")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("Berlin")
    archiveVersion.set("${project.version}")
    minimize()

    relocate("de.tr7zw.changeme.nbtapi", "net.cyruspvp.hub.libs.nbtapi")
    relocate("com.cryptomorin.xseries", "net.cyruspvp.hub.libs.xseries") // Relocando correctamente XSeries
    relocate("fr.mrmicky.fastparticles", "net.cyruspvp.hub.libs.fastparticles")
    relocate("com.github.retrooper", "xyz.refinedev.lib.com.github.retrooper")
}

fun Map<String, Any>.toYaml(writer: StringWriter, indent: String = "") {
    for ((key, value) in this) {
        when (value) {
            is String -> writer.write("$indent$key: $value\n")
            is List<*> -> {
                writer.write("$indent$key:\n")
                value.forEach { item -> writer.write("$indent  - $item\n") }
            }
            is Map<*, *> -> {
                writer.write("$indent$key:\n")
                (value as Map<String, Any>).toYaml(writer, "$indent  ")
            }
        }
    }
}

configureNexusPublishing()