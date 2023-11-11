package com.example.data_sources.server_gateway

import DataSources
import com.example.NotConnectedException
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL = "http://api.themoviedb.org/3/"
const val API_KEY_VALUE = "e0e450709bf35bc741a26a4ffd9e3116"
const val API_KEY_NAME = "api_key"

internal val httpClient = HttpClient(CIO) {
    install(DefaultRequest) {
        url(BASE_URL)
        headers.apply {
            append("Content-type", "application/json")
        }
        url.parameters.apply {
            append(API_KEY_NAME, API_KEY_VALUE)
            append("language", "en-US")
        }
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}.apply {
    plugin(HttpSend).intercept { request ->
        if (DataSources.networkStatus?.isConnected() == false) throw NotConnectedException()
        execute(request)
    }
}