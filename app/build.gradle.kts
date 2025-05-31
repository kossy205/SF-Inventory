plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.kosiso.sfinventory"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kosiso.sfinventory"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // compose constraint Layout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    // ViewModel support in Compose
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Kotlin Coroutines for ViewModel and StateFlow
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Hilt dependencies
    implementation ("com.google.dagger:hilt-android:2.48.1")
    kapt ("com.google.dagger:hilt-compiler:2.48.1")

    // ViewModel with Hilt integration
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")

    // lifecycle service
    implementation("androidx.lifecycle:lifecycle-service:2.8.7")

    implementation ("androidx.compose.runtime:runtime-livedata:1.7.6")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    implementation ("com.google.code.gson:gson:2.10.1")

    // Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")




}