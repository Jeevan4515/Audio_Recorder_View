// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.4")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
true // Needed to make the Suppress annotation work for the plugins block