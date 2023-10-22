plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    libs.versions.apply {
        buildToolsVersion = buildTools.get()
        compileSdk = compileSdkVersion.get().toInt()
        namespace = "com.example.moviesapp"

        defaultConfig {
            applicationId = "com.example.moviesapp"
            minSdk = minSdkVersion.get().toInt()
            targetSdk = compileSdkVersion.get().toInt()
            versionCode = versionCodeInt.get().toInt()
            versionName = versionNameString.get()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jdkVersion.get()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":domain"))
    implementation(project(":entities"))

    implementation(libs.bundles.androidUi)
    implementation(libs.kotlin.jdk)
    implementation(libs.material)
    implementation(libs.glide)
    annotationProcessor(libs.glide.annotationProcessor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidAutomation)

    implementation(files("src/libs/YouTubeAndroidPlayerApi.jar"))
}
