plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rezafaaldiansyah_5210411314.inventoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rezafaaldiansyah_5210411314.inventoryapp"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase BOM - No need to specify versions for Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-database:20.2.2")


    // Play Services Auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // AndroidX Libraries
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.12.0")

    // Networking
    implementation("com.android.volley:volley:1.2.1")

    // QR Code Scanning
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.itextpdf:itextg:5.5.10")
}
