import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "sdmed.extra.cso"
    compileSdk = 35

    defaultConfig {
        applicationId = "sdmed.extra.cso"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        multiDexEnabled = true
        val googleApiKey: String by project
        resValue("string", "googleApiKey", googleApiKey)
        buildConfigField("String", "googleApiKey", googleApiKey)
        manifestPlaceholders.put("googleApiKey", googleApiKey)
    }
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res",
                "src/main/res/layouts",
                "src/main/res/layouts/activity",
                "src/main/res/layouts/fragment",
                "src/main/res/layouts/dialog",

                "src/main/res/layouts/common",
                "src/main/res/layouts/landing",
                "src/main/res/layouts/login",
                "src/main/res/layouts/media",
                "src/main/res/layouts/hospitalMap",
                "src/main/res/layouts/hospitalMap/hospitalFind",
                "src/main/res/layouts/hospitalMap/hospitalDetail",
                "src/main/res/layouts/hospitalMap/hospitalPharmacyFind",

                "src/main/res/layouts/main",
                "src/main/res/layouts/main/edi",
                "src/main/res/layouts/main/price",
                "src/main/res/layouts/main/home",
                "src/main/res/layouts/main/qna",
                "src/main/res/layouts/main/my",

                "src/main/res/layouts/dialog/loading",
                "src/main/res/layouts/dialog/toast",
                "src/main/res/layouts/dialog/message",
                "src/main/res/layouts/dialog/calendar",
                "src/main/res/layouts/dialog/select",
                "src/main/res/layouts/dialog/pharmaSelect",
                "src/main/res/layouts/dialog/hospitalTemp",

                "src/main/res/drawables",
                "src/main/res/drawables/shape",
                "src/main/res/drawables/selector",
                "src/main/res/drawables/vector",
                "src/main/res/drawables/image"
            )
        }
    }

    signingConfigs {
        val keyStore: String by project
        val keyStorePassword: String by project
        val keyAlias: String by project
        val keyPassword: String by project
        create("releaseWithKey") {
            storeFile = file(keyStore)
            storePassword = keyStorePassword
            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("releaseWithKey")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("releaseWithKey")
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("kr") {
            dimension = "version"
            applicationId = "kr.sdmed.extra.cso"
            resValue("string", "dynamic_app_name", "@string/app_name")
        }
        create("dev") {
            dimension = "version"
            applicationId = "kr.sdmed.extra.cso.dev"
            resValue("string", "dynamic_app_name", "@string/app_name_dev")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment)
    implementation(libs.media3.player.ui)
    implementation(libs.media3.player.common)
    implementation(libs.media3.player.exoplayer)
    implementation(libs.material)
    implementation(libs.jetbrains.kotlin.std)
    implementation(libs.jetbrains.coroutines.core)
    implementation(libs.jetbrains.coroutines.android)

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.uiKtx)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.security.cryptoKtx)

    implementation(libs.jwt.decode)

    implementation(libs.androidx.paging.runtimeKtx)
    implementation(libs.androidx.paging.commonKtx)
    implementation(libs.androidx.paging.guava)

    implementation(libs.kodein.core)
    implementation(libs.kodein.framework)
    implementation(libs.kodein.generic)

    implementation(libs.squareup.retrofit) {
        exclude(module = "okhttp")
    }
    implementation(libs.squareup.retrofit.scalars)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.log)

    implementation(libs.tedpermission)

    implementation(libs.glide)
    implementation(libs.glide.okhttp)
    kapt(libs.glide.compiler)
    implementation(libs.webpdecoder)

    implementation(libs.androidx.multidex)

    implementation(libs.greenrobot.eventbus)

    implementation(libs.fasterxml.jacson)
    implementation(libs.hivemq.client)

    implementation(libs.google.play)
    implementation(libs.google.location)
    implementation(libs.google.maps)
    implementation(libs.google.map.util)
}