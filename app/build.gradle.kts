import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}


val keystoreProperties = if (rootProject.file("local.properties").exists()) {
    Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }
} else {
    Properties() // Fallback for CI/CD without local.properties
}

val myKeystoreFilePath = System.getenv("KEYSTORE_PATH") ?: "../KeyStore.jks"
val myStorePassword = System.getenv("KEYSTORE_PASSWORD") ?: keystoreProperties["storePassword"] as String
val myKeyAlias = System.getenv("KEY_ALIAS") ?: keystoreProperties["keyAlias"] as String
val myKeyPassword = System.getenv("KEY_PASSWORD") ?: keystoreProperties["keyPassword"] as String

android {
    namespace = "com.thit.androidcicd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.thit.androidcicd"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(myKeystoreFilePath)
            storePassword = myStorePassword
            keyAlias = myKeyAlias
            keyPassword = myKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}