// Top-level build file where you can add configuration options common to all sub-projects/modules.
val room_version by extra { "2.6.0" }
val kotlin_version by extra { "1.9.10" }
val junit_version by extra { "4.13.2" }
val build_tools_version by extra { "34.0.0" }
val version_name by extra { "1.0" }
val app_varsion_code by extra { 1 }
val compile_version by extra { 34 }
val minimum_version by extra { 21 }

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    kotlin("jvm") version "1.9.10"
}