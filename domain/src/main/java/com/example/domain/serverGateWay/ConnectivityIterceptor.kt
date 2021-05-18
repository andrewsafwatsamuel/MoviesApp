package com.example.domain.serverGateWay

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.NoConnectivityException
import com.example.domain.engine.Domain
import okhttp3.Interceptor
import okhttp3.Response

fun createConnectivityInterceptor(
    chain: Interceptor.Chain,
    context: Context = Domain.applicationContext,
    isConnected: (Context) -> Boolean = ::checkConnectivity
): Response = with(chain.request()) {
    url().newBuilder()
        .build()
        .takeIf { isConnected(context) }
        ?.let { url -> newBuilder().url(url).build() }
        ?.let(chain::proceed) ?: throw NoConnectivityException()
}

fun checkConnectivity(context: Context) = with(context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) checkConnectivityBelowMarshmello()
    else checkConnectivityMarshmello()
}

@Suppress("DEPRECATION")
fun Context.checkConnectivityBelowMarshmello(
    connectivityManager: ConnectivityManager = getConnectivityManager()
) = connectivityManager.activeNetworkInfo
    ?.run {
        (type == ConnectivityManager.TYPE_WIFI) or (type == ConnectivityManager.TYPE_MOBILE)
    } ?: false

@RequiresApi(Build.VERSION_CODES.M)
fun Context.checkConnectivityMarshmello(
    connectivityManager: ConnectivityManager = getConnectivityManager()
) = connectivityManager.activeNetwork
    ?.let(connectivityManager::getNetworkCapabilities)
    ?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    } ?: false

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
