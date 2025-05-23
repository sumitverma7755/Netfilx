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

# ExoPlayer ProGuard rules
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**
-keepclassmembers class com.google.android.exoplayer2.** { *; }

# ExoPlayer IMA extension
-keep class com.google.ads.interactivemedia.** { *; }
-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }

# Cache related classes
-keep class com.google.android.exoplayer2.upstream.cache.** { *; }
-keep class com.google.android.exoplayer2.database.** { *; }

# Keep download service and components
-keep class com.example.netfix.video.** { *; }
-keepclassmembers class com.example.netfix.video.** { *; }