package com.moviesapp.data_sources.server_gateway.di

import com.moviesapp.data_sources.server_gateway.api.MoviesAPI
import com.moviesapp.data_sources.server_gateway.api.MoviesAPIImpl
import com.moviesapp.data_sources.server_gateway.httpClient

object ServerGatewayServiceLocator {
    val moviesAPI: MoviesAPI by lazy { MoviesAPIImpl(httpClient) }
}