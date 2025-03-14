# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn io.netty.channel.epoll.**
-dontwarn io.netty.handler.codec.http.**
-dontwarn io.netty.handler.proxy.**
-dontwarn io.netty.internal.tcnative.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.logging.log4j.**
-dontwarn org.eclipse.jetty.**
-dontwarn org.slf4j.**
-dontwarn reactor.blockhound.integration.BlockHoundIntegration
-dontwarn com.google.appengine.api.urlfetch.**

-keepattributes SourceFile, LineNumberTable
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
-keep class * extends com.google.gson.TypeAdapter
-keep, allowobfuscation, allowoptimization class org.kodein.di.TypeReference
-keep, allowobfuscation, allowoptimization class * extends org.kodein.di.TypeReference
-keep, allowobfuscation, allowshrinking interface retrofit2.Call
-keep, allowobfuscation, allowshrinking class retrofit2.Response
-keep, allowobfuscation, allowshrinking class kotlin.coroutines.Continuation
-keep, allowobfuscation, allowshrinking class kotlinx.coroutines.flow.Flow
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }
-keep class com.hivemq.** { *; }
-keep class sdmed.extra.cso.models.* { *; }
-keep class sdmed.extra.cso.models.common.* { *; }
-keep class sdmed.extra.cso.models.eventbus.* { *; }
-keep class sdmed.extra.cso.models.mqtt.* { *; }
-keep class sdmed.extra.cso.models.repository.* { *; }
-keep class sdmed.extra.cso.models.retrofit.** { *; }
-keep class sdmed.extra.cso.models.services.* { *; }
-keep interface sdmed.extra.cso.interfaces.* { *; }
-keep interface sdmed.extra.cso.interfaces.repository.* { *; }
-keep interface sdmed.extra.cso.interfaces.services.* { *; }
-keep public class sdmed.extra.cso.databinding.* {
    public void setDataContext(*);
}
-keepclassmembernames class io.netty.** { *; }
-keepclassmembernames class org.jctools.** { *; }

-keep class com.google.maps.** { *; }