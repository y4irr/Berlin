import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.cyruspvp.hub"
version = "1.1"

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
    compileOnly("com.github.retrooper.packetevents:spigot:2.2.1") {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "org.jetbrains", module = "annotations")
    }

    compileOnly("com.lunarclient:apollo-api:1.1.5")
    compileOnly("com.lunarclient:apollo-extra-adventure4:1.1.6")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("us.ajg0702.queue.api:api:2.0.7")

    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT") {
        exclude(group = "com.mojang", module = "authlib")
        exclude(group = "com.mojang", module = "datafixerupper")
        exclude(group = "com.mojang", module = "logging")
    }

    compileOnly(fileTree("libs/spigots") { include("1.20.4.jar", "1.8.jar") })
    compileOnly("it.unimi.dsi:fastutil:8.5.12")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly(fileTree("libs/ranks") { include("VolcanoAPI.jar", "AquaCoreAPI.jar", "HeliumAPI-1.0-SNAPSHOT.jar") })
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
    compileOnly("com.github.cryptomorin:XSeries:9.9.0")
    compileOnly("fr.mrmicky:FastParticles:2.0.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    archiveBaseName.set("Berlin")
    archiveVersion.set("1.1")
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
    archiveVersion.set("1.1")
    minimize()

    relocate("de.tr7zw.changeme.nbtapi", "dev.comunidad.net.libs.nbtapi")
    relocate("com.cryptomorin.xseries", "dev.comunidad.net.libs.xseries")
    relocate("fr.mrmicky.fastparticles", "dev.comunidad.net.libs.fastparticles")
    relocate("com.github.retrooper", "xyz.refinedev.lib.com.github.retrooper")
}
