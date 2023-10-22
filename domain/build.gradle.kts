plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.domain"
    libs.versions.apply {
        buildToolsVersion = buildTools.get()
        compileSdk = compileSdkVersion.get().toInt()
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

    implementation(project(":entities"))

    api(libs.bundles.rx)
    api(libs.androidx.lifecycleExtensions)
    api(libs.android.arch.lifcycleCommon)
    implementation(libs.bundles.retrofit)
    implementation(libs.androidx.core)
    implementation(libs.kotlin.jdk)
    ksp(libs.room.annotationProcessor)

    testImplementation(libs.bundles.unitTesting)
}
