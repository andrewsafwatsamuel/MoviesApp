plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    namespace = "com.example.domain"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    testImplementation("junit:junit:4.13.2")
    implementation(project(":entities"))
    api("io.reactivex.rxjava2:rxjava:2.2.9")
    api("io.reactivex.rxjava2:rxandroid:2.1.1")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("android.arch.lifecycle:common:1.1.1")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    api("com.squareup.retrofit2:adapter-rxjava2:2.5.0")

    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
    ksp("androidx.room:room-compiler:2.6.0")

    testImplementation("android.arch.core:core-testing:1.1.1") {
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "support-annotations")
        exclude(group = "com.android.support", module = "support-core-utils")
    }

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
    testImplementation("org.mockito:mockito-inline:2.13.0")

}
