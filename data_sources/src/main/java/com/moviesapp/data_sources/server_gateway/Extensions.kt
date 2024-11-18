package com.moviesapp.data_sources.server_gateway

import com.moviesapp.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.toResponse(): Response<T> =
    Response(body<T>(), status.value, status.description)