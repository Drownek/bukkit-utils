plugins {
    java
    id("maven-publish")
}

group = "me.drownek"
version = "2.0"

dependencies {
    implementation("com.github.cryptomorin:XSeries:10.0.0")

    implementation("dev.triumphteam:triumph-gui:3.1.7")

    // adventure
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")

    implementation("org.jetbrains:annotations:20.1.0")

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.3")

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
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}