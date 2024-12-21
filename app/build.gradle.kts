@file:Suppress("UNUSED_EXPRESSION")

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ecuacion.finalproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ecuacion.finalproject"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            viewBinding = true
            dataBinding = true
        }


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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.recyclerview:recyclerview:1.3.2") //viewing
    implementation("com.google.firebase:firebase-firestore:25.1.1") // stoting
    implementation("com.google.firebase:firebase-storage:21.0.1")   //updating

    implementation ("com.firebaseui:firebase-ui-database:8.0.0")
    implementation ("com.google.firebase:firebase-database:21.0.0")
    implementation ("com.orhanobut:dialogplus:1.11@aar")



    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.29")//gif

//    configurations.implementation {
//        exclude(group = "org.jetbrains.kotlin", module= "kotlin-stdlib-jdk8")
//    }


}