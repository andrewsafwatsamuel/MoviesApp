plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlin-android")
}
android {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    namespace = "com.example.entities"

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
    api("androidx.room:room-runtime:2.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
    implementation("com.google.code.gson:gson:2.10.1")

}