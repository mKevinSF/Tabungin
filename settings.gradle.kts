pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        jcenter()
        maven { url = uri("https://www.jitpack.io" ) }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        jcenter()
        maven { url = uri("https://www.jitpack.io" ) }
        google()
        mavenCentral()
    }
}

rootProject.name = "IMAGE TO TEXT APP"
include(":app")
 