plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.moviesapp.entities"
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

    api(libs.room.runtime)
    implementation(libs.kotlin.jdk)
    implementation(libs.ktor.jsonSerialization)

    testImplementation(libs.junit)
}