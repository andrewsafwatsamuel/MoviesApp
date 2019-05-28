package com.example.domain.serverGateWay

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val API_ROOT = "http://api.themoviedb.org/3/"
const val API_KEY="b3070a5d3abfb7c241d2688d066914e7"

object RetrofitInstance {
    operator fun invoke(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ROOT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}