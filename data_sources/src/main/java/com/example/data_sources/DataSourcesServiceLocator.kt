package com.example.data_sources

import com.example.data_sources.repositories.MoviesRepository
import com.example.data_sources.repositories.MoviesRepositoryImpl
import com.example.data_sources.repositories.ResponseToSingleMapper
import com.example.data_sources.server_gateway.di.ServerGatewayServiceLocator

object DataSourcesServiceLocator {
    val moviesRepository: MoviesRepository by lazy {
        MoviesRepositoryImpl(ServerGatewayServiceLocator.moviesAPI, ResponseToSingleMapper())
    }
}