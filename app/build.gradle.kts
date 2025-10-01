import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "programmazionemobile.esercizi.personalcodex"
    compileSdk = 35

    defaultConfig {
        applicationId = "programmazionemobile.esercizi.personalcodex"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.cardview)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)

    //noinspection Aligned16KB
    implementation("com.google.mediapipe:tasks-text:0.10.15")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}