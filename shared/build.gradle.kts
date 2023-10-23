plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.gms.google-services")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 33
    defaultConfig {
        minSdk = 27
    }
}

dependencies {
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("dev.gitlive:firebase-firestore:1.8.1") // This line
    implementation("dev.gitlive:firebase-common:1.8.1")// This line
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1") // This line
    implementation("dev.gitlive:firebase-auth:1.10.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-common-ktx:20.3.3")
}