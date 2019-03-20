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

-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keep class * extends com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewAdapter{*;}
-keep class com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewAdapter{*;}
-keep class * extends com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder{*;}
-keep class com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder{*;}
-keep class com.zjzf.shoescircle.lib.base.baseadapter.** { *; }
-keep @com.zjzf.shoescircle.lib.base.baseadapter.LayoutId class *
-keepclassmembers class ** {
    @com.zjzf.shoescircle.lib.base.baseadapter.LayoutId *;
}
-keep class com.zjzf.shoescircle.lib.utils.joor.** { *; }

