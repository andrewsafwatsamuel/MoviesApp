// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra.apply {
        set("agp_version", "8.1.2")
        set("room_version", "2.6.0")
        set("kotlin_version", "1.9.10")
        set("junit_version", "4.13.2")
        set("build_tools_version", "34.0.0")
        set("version_name", "1.0")
        set("app_varsion_code", 1)
        set("compile_version", 34)
        set("minimum_version", 21)
    }
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${project.extra.get("agp_version")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra.get("kotlin_version")}")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.10-1.0.13")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}