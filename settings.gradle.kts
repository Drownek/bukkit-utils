dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            name = "jitpack-repo"
            url = uri("https://jitpack.io")
        }
        maven {
            name = "enginehub-maven"
            url = uri("https://maven.enginehub.org/repo/")
        }
        maven {
            name = "storehouse-releases"
            url = uri("https://storehouse.okaeri.eu/repository/maven-releases/")
        }
        maven {
            name = "panda-repo"
            url = uri("https://repo.panda-lang.org/releases")
        }
    }
}