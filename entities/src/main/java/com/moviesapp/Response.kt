package com.moviesapp

data class Response<T>(
    val body:T?,
    val statusCode:Int?,
    val description:String?
)