import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
}

val clientId = getLocalPropertyKey("CLIENT_ID")
val tmapAppKey = getLocalPropertyKey("TMAP_APP_KEY")
val searchAPIClientID = getLocalPropertyKey("SEARCH_API_CLIENT_ID")
val searchAPIClientSecret = getLocalPropertyKey("SEARCH_API_CLIENT_SECRET")
val odsayAppKey = getLocalPropertyKey("ODSAY_APP_KEY")
val openDataPotalKey = getLocalPropertyKey("OPEN_DATA_POTAL_KEY")

android {

    namespace = "com.hansung.sherpa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hansung.sherpa"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "CLIENT_ID", clientId)
        buildConfigField("String", "TMAP_APP_KEY", tmapAppKey)
        buildConfigField("String", "SEARCH_API_CLIENT_ID", searchAPIClientID)
        buildConfigField("String", "SEARCH_API_CLIENT_SECRET", searchAPIClientSecret)
        buildConfigField("String", "ODSAY_APP_KEY", odsayAppKey)
        buildConfigField("String", "OPEN_DATA_POTAL_KEY", openDataPotalKey)
        manifestPlaceholders["CLIENT_ID"] = clientId

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            merges += "META-INF/spring.*"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/notice.txt"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun getLocalPropertyKey(propertyKey : String) : String{
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.filament.android)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // for naver map SDK
    implementation("com.naver.maps:map-sdk:3.18.0")

    // for api request
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    //for User Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // for Material Design 3
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.core:core-ktx:1.0.2")

    // for preference category
    implementation ("androidx.preference:preference-ktx:1.2.1")

    // for Room Datanbase
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.8.1")

    implementation("io.github.fornewid:naver-map-compose:1.7.2")
    implementation("io.github.fornewid:naver-map-location:21.0.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // for Compose tooling
    // https://developer.android.com/develop/ui/compose/tooling?hl=ko
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // for Barchart in Routes UI
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // MapStruct dependencies
    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor)

    // spring-context
    implementation(libs.spring.context)


    // for Extendable View
    implementation("com.github.skydoves:expandablelayout:1.0.7")
}