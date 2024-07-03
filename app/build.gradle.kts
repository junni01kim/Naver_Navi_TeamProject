import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

val clientId = getClientId("CLIENT_ID")
val tmapAppKey = getTmapAppKey("TMAP_APP_KEY")
val searchAPIClientID = getSearchAPIClientID("SEARCH_API_CLIENT_ID")
val searchAPIClientSecret = getSearchAPIClientSecret("SEARCH_API_CLIENT_SECRET")
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
        manifestPlaceholders["CLIENT_ID"] = clientId
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

fun getClientId(propertyKey : String) : String{
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

fun getTmapAppKey(propertyKey : String) : String{
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

fun getSearchAPIClientID(propertyKey : String) : String{
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

fun getSearchAPIClientSecret(propertyKey : String) : String{
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.map.sdk)
    // for api request
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)

    //for User Location
    implementation(libs.play.services.location)

    implementation(libs.androidx.preference.ktx)
}