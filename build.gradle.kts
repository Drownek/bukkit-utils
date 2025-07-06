plugins {
    `java-library`
    id("maven-publish")
}

group = "me.drownek"
version = "1.0.4-dev"

dependencies {
    api("com.github.cryptomorin:XSeries:10.0.0")

    api("dev.triumphteam:triumph-gui:3.1.11")

    // adventure
    api("net.kyori:adventure-api:4.16.0")
    api("net.kyori:adventure-text-serializer-legacy:4.16.0")
    api("net.kyori:adventure-text-minimessage:4.16.0")
    api("net.kyori:adventure-platform-bukkit:4.2.0")

    api("org.jetbrains:annotations:20.1.0")

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    api("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.3")

    // ItemsAdder
    compileOnly("com.github.LoneDev6:api-itemsadder:3.6.1")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            println("Publishing as ${listOf(groupId, artifactId, version).joinToString(":") { it ?: "NONE"}}")
            from(components["java"])
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}