package com.example

data class Response<T>(
    val body:T?,
    val statusCode:Int?,
    val description:String?
)