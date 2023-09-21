plugins {
    id("com.android.application")
}

android {
    namespace = "com.otemainc.ngugehillslodgepos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.otemainc.ngugehillslodgepos"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.9.0")
    }
}

dependencies {

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.2")
    implementation("androidx.navigation:navigation-ui:2.7.2")
    // dependency for slider view
    implementation("com.github.smarteist:autoimageslider:1.3.9")
    // dependency for loading image from url
    implementation("com.github.bumptech.glide:glide:4.12.0")
    //ticker textview
    implementation("com.tomer:fadingtextview:2.5")
    //dots loader
    implementation ("com.agrawalsuneet.androidlibs:dotsloader:1.4")
    //permissions manager
    implementation ("com.nabinbhandari.android:permissions:3.8")
    // update manager
    implementation ("com.google.android.play:core:1.10.3")
    //Toasty
    implementation ("com.github.GrenderG:Toasty:1.2.5")
    //Alerts and dialogues
    implementation ("com.geniusforapp.fancydialog:FancyDialog:0.1.4")
    implementation ("com.github.f0ris.sweetalert:library:1.5.1")
    //progressbar
    implementation ("com.kaopiz:kprogresshud:1.1.0")
    //tap target
    implementation ("com.getkeepsafe.taptargetview:taptargetview:1.11.0")
    //crossfade drawer
    implementation ("com.mikepenz:crossfadedrawerlayout:1.0.1@aar")
    //circle image view
    implementation ("de.hdodenhof:circleimageview:2.2.0")
    //volley
    implementation ("com.android.volley:volley:1.2.1")
    //guava
    implementation ("com.google.guava:guava:27.0.1-android")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}