package dev.y4irr.repository

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.util.Properties

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ buildSrc
 * @author Yair © 2025
 * Date: 05 - feb.
 */

object RepositoryConfiguration {
    private val properties: Properties = Properties()

    init {
        val gradleUserHome = System.getenv("GRADLE_USER_HOME") ?: "${System.getProperty("user.home")}/.gradle"
        val propertiesFile = File("$gradleUserHome/gradle.properties")

        if (propertiesFile.exists()) {
            properties.load(propertiesFile.inputStream())
        }
    }

    fun getUsername(): String? {
        return properties.getProperty("cyrusUsername") ?: System.getenv("CYRUS_USERNAME")
    }

    fun getPassword(): String? {
        return properties.getProperty("cyrusPassword") ?: System.getenv("CYRUS_PASSWORD")
    }
}

fun Project.configureNexusPublishing() {
    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "nexus"
                url = uri("http://54.39.130.183:8081/repository/maven-releases/")
                isAllowInsecureProtocol = true

                credentials {
                    username = RepositoryConfiguration.getUsername()
                    password = RepositoryConfiguration.getPassword()
                }
            }
        }
    }
}