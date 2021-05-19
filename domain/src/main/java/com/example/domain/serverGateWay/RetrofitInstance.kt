package com.example.domain.serverGateWay

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


const val API_ROOT = "http://api.themoviedb.org/3/"
const val API_KEY_VALUE = "e0e450709bf35bc741a26a4ffd9e3116"
const val API_KEY_NAME = "api_key"

private val httpClient by lazy {
    OkHttpClient().newBuilder()
        .addInterceptor { request(it) }
        .addInterceptor {
            createConnectivityInterceptor(it)
        }
        .build()
}

private fun request(chain: Interceptor.Chain): Response = with(chain.request()) {
    url().newBuilder()
        .addQueryParameter(API_KEY_NAME, API_KEY_VALUE)
        .build()
        .let { url -> newBuilder().url(url).build() }
        .let { request -> chain.proceed(request) }
}

val retrofitInstance: Retrofit by lazy {
      Retrofit.Builder()
          .baseUrl(API_ROOT)
          .client(httpClient)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()

  }
