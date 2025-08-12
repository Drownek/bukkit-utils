# bukkit-utils
A modern utility library for Minecraft Bukkit/Spigot plugin developers, providing convenient APIs and ready-to-use solutions for common development tasks.

It's used in [light-platform](https://github.com/Drownek/light-platform), but can be also used standalone with proper setup. To do that, see instructions in `Installation`.

## 📦 Installation
### Gradle (Kotlin DSL)
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Drownek:bukkit-utils:1.0.6")
}
```
### Gradle (Groovy)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Drownek:bukkit-utils:1.0.6'
}
```
### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Drownek</groupId>
        <artifactId>bukkit-utils</artifactId>
        <version>1.0.6</version>
    </dependency>
</dependencies>
```

To use most of the classes meant to be serializable in okaeri-configs, you need to use `BukkitUtilsSerdes` serdes pack when creating config.
```java
TestConfig config = ConfigManager.create(TestConfig.class, (it) -> {
    it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new BukkitUtilsSerdes()); // specify configurer implementation, optionally additional serdes packages
    it.withBindFile(new File(this.getDataFolder(), "config.yml")); // specify Path, File or pathname
    it.withRemoveOrphans(true); // automatic removal of undeclared keys
    it.saveDefaults(); // save file if does not exists
    it.load(true); // load and save to update comments/new fields 
});
```

## 🎯 Requirements
- **Java 16+**
- **Spigot/Paper 1.8+**

## 🤝 Support
If you have some issues, feel free to reach me on discord: `drownek` or create an issue in GitHub repository!

## 🏗️ Building
```bash
# Clone the repository
git clone https://github.com/drownek/bukkit-utils.git
cd bukkit-utils

# Build the project
./gradlew build

# Publish to local Maven repository
./gradlew publishToMavenLocal
```

## 🌍 Using Localization
To change the default value of `MessageKey`, call `LocalizationManager.setMessage(MessageKey key, String value)`

## 🔗 Useful Links
- [Adventure API Docs](https://docs.adventure.kyori.net/)
- [Triumph GUI Docs](https://triumphteam.dev/docs/triumph-gui/introduction)
- [XSeries Wiki](https://github.com/CryptoMorin/XSeries/wiki)
- [Spigot API](https://hub.spigotmc.org/javadocs/spigot/)
- [Okaeri Configs](https://github.com/OkaeriPoland/okaeri-configs)

## 📜 License

Project is licensed under [MIT](https://choosealicense.com/licenses/mit/).

This means that...

- ✅ You can freely use, copy, modify, and distribute this project, even for commercial purposes.
- 🧾 You **must include the original license and copyright notice** in any copies or substantial portions.
- ❌ The software is provided **"as is"**, without warranty of any kind. The author is **not liable** for any damages or issues caused by using it.

