plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.quiz.application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.quiz.application"
        minSdk = 24
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /////
    //Dimen
    implementation ("com.intuit.ssp:ssp-android:1.0.5")
    implementation ("com.intuit.sdp:sdp-android:1.0.5")
    implementation ("com.airbnb.android:lottie:3.4.0")   // lottie
    implementation ("com.scwang.wave:MultiWaveHeader:1.0.0")   // multiwave header
    implementation("com.google.code.gson:gson:2.11.0")  // gson

    implementation("com.github.AsynctaskCoffee:tinderlikecardstack:1.0")  // cardswipe
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.22")  // gif
}