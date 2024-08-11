plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.9.0"
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.yardsync"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.yardsync"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.ui.unit.android)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Gif View
    implementation(libs.android.gif.drawable)

    //Custom Bottom Navigation Bar
    implementation(libs.curved.bottom.navigation)

    //Supabase
    implementation(libs.postgrest.kt)
    implementation(libs.gotrue.kt)
    implementation(libs.storage.kt)
    implementation(libs.ktor.client.android)

    //Datastore
    implementation(libs.androidx.datastore.preferences)

    //Coil
    implementation(libs.coil)

    //CarouselView
    implementation(libs.carouselview)

    //State Progress
    implementation(libs.stateprogressbar.v009)

    //Custom Spinner
    implementation(libs.dropsy)

    //Safe Args
    implementation(libs.androidx.navigation.safe.args.gradle.plugin)

    //QR Code Scanner
    implementation(libs.code.scanner)

    //QR Code Generator
    implementation(libs.custom.qr.generator)
}