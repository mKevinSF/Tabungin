buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false

    // Google Services
    id("com.google.gms.google-services") version "4.4.2" apply false
<<<<<<< HEAD
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
=======
>>>>>>> 88b8573 (m)
}