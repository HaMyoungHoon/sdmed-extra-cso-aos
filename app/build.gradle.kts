plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "sdmed.extra.cso"
    compileSdk = 35

    defaultConfig {
        applicationId = "sdmed.extra.cso"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        multiDexEnabled = true
    }
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res",
                "src/main/res/layouts",
                "src/main/res/layouts/activity",
                "src/main/res/layouts/activity/main",
                "src/main/res/layouts/fragment/main/edi",
                "src/main/res/layouts/fragment/main/price",
                "src/main/res/layouts/fragment/main/home",
                "src/main/res/layouts/fragment/main/qna",
                "src/main/res/layouts/fragment/main/my",
                "src/main/res/layouts/activity/landing",
                "src/main/res/layouts/activity/login",

                "src/main/res/layouts/fragment",
                "src/main/res/layouts/dialog",
                "src/main/res/layouts/dialog/loading",
                "src/main/res/layouts/dialog/toast",
                "src/main/res/layouts/dialog/message",

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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment)
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
    implementation(libs.glide.compiler)

    implementation(libs.androidx.multidex)

    implementation(libs.greenrobot.eventbus)
}