package com.fphoenixcorneae.core.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.*


//这个扩展类添加了一个名为 logLifecycleEvents() 的函数，它注册了一个 ActivityLifecycleCallbacks 对象，
//以便在 Activity 生命周期事件发生时记录日志。要在应用程序中使用此扩展类，只需在 Application 类的实例上调用
// logLifecycleEvents() 函数即可。
//
//例如，在 MyApplication 类中使用 logLifecycleEvents()：
//class MyApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        logLifecycleEvents()
//    }
//}
/**
 * 扩展 Application 类，添加日志记录功能。
 */
fun Application.logLifecycleEvents() {
    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d("ActivityLifecycle", "onActivityCreated: ${activity.localClassName}")
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d("ActivityLifecycle", "onActivityStarted: ${activity.localClassName}")
        }

        override fun onActivityResumed(activity: Activity) {
            Log.d("ActivityLifecycle", "onActivityResumed: ${activity.localClassName}")
        }

        override fun onActivityPaused(activity: Activity) {
            Log.d("ActivityLifecycle", "onActivityPaused: ${activity.localClassName}")
        }

        override fun onActivityStopped(activity: Activity) {
            Log.d("ActivityLifecycle", "onActivityStopped: ${activity.localClassName}")
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            Log.d("ActivityLifecycle", "onActivitySaveInstanceState: ${activity.localClassName}")
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d("ActivityLifecycle", "onActivityDestroyed: ${activity.localClassName}")
        }
    })
}


//这个扩展类添加了一个名为 isNetworkConnected() 的函数，它使用 Android 框架提供的 ConnectivityManager 来检查当前设备是否连接到互联网。
//该函数在 Android M 及以上版本中使用 NetworkCapabilities 类来检查互联网连接，而在 Android L 及以下版本中使用 NetworkInfo 类。
//
//要在应用程序中使用此扩展类，只需在 Application 类的实例上调用 isNetworkConnected() 函数即可。
//
//例如，在 MyApplication 类中使用 isNetworkConnected()：
//class MyApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        val isNetworkConnected = isNetworkConnected()
//        if (isNetworkConnected) {
//            // 执行需要互联网连接的操作
//        } else {
//            // 显示无网络连接的错误消息
//        }
//    }
//}
/**
 * 扩展 Application 类，添加检查网络连接的功能。
 */
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun Application.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager?.activeNetworkInfo ?: return false
        networkInfo.isConnected
    }
}

//这个扩展类添加了两个函数：setLocale() 和 getLocale()。setLocale() 函数使用 Configuration 类设置应用程序的默认语言，并在运行时应用更改。getLocale() 函数返回当前应用程序语言。
//
//要在应用程序中使用此扩展类，只需在 Application 类的实例上调用这些函数即可。
//
//例如，在 MyApplication 类中使用 setLocale() 和 getLocale()：
//class MyApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        // 设置应用程序语言为中文
//        val locale = Locale("zh", "CN")
//        setLocale(locale)
//
//        // 获取当前应用程序语言
//        val currentLocale = getLocale()
//        Log.d("Locale", "当前应用程序语言：${currentLocale.language}_${currentLocale.country}")
//    }
//}

/**
 * 扩展 Application 类，添加设置应用程序语言的功能。
 */
fun Application.setLocale(locale: Locale) {
    resources.configuration.setLocale(locale)
    applicationContext.createConfigurationContext(Configuration())
}

/**
 * 扩展 Application 类，添加获取应用程序语言的功能。
 */
fun Application.getLocale(): Locale {
    return resources.configuration.locale
}

//这个扩展类添加了两个函数：getVersionCode() 和 getVersionName()。这些函数使用 PackageManager 获取应用程序的版本号和版本名称。
//
//要在应用程序中使用此扩展类，只需在 Application 类的实例上调用这些函数即可。
//
//例如，在 MyApplication 类中使用 getVersionCode() 和 getVersionName()：
//class MyApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        // 获取应用程序的版本号和版本名称
//        val versionCode = getVersionCode()
//        val versionName = getVersionName()
//        Log.d("Version", "应用程序版本号：$versionCode，版本名称：$versionName")
//    }
//}

/**
 * 扩展 Application 类，添加获取应用程序版本号和版本名称的功能。
 */
fun Application.getVersionCode(): Int {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionCode
}

fun Application.getVersionName(): String {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName
}

//这个扩展类添加了一个 setupGlobalExceptionHandler() 方法，用于设置全局异常处理程序。在这个方法中，我们使用 Thread.setDefaultUncaughtExceptionHandler() 方法来设置一个新的未捕获异常处理程序。在这个处理程序中，我们可以处理未捕获的异常，并添加自定义的异常处理逻辑。
//
//要在应用程序中使用此扩展，只需在 Application 类的实例上调用 setupGlobalExceptionHandler() 方法即可。
//
//例如，在 MyApplication 类中使用 setupGlobalExceptionHandler()：
//class MyApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        // 设置全局异常处理
//        setupGlobalExceptionHandler { t, e ->
//            // 这里添加应用程序的其他初始化逻辑
//        }
//    }
//}

/**
 * 扩展 Application 类，添加全局异常处理的功能。
 */
fun setupGlobalExceptionHandler(block: (Thread, Throwable) -> Unit) {
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        // 处理未捕获的异常
        Log.e("UncaughtException", e.toString())

        // 可以在这里添加自定义的异常处理逻辑
        block(t, e)
    }
}




