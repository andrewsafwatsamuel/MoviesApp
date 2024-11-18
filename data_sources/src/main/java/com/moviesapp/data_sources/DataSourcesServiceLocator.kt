package com.moviesapp.data_sources

import com.moviesapp.data_sources.repositories.MoviesRepository
import com.moviesapp.data_sources.repositories.MoviesRepositoryImpl
import com.moviesapp.data_sources.repositories.ResponseToSingleMapper
import com.moviesapp.data_sources.server_gateway.di.ServerGatewayServiceLocator

object DataSourcesServiceLocator {
    val moviesRepository: MoviesRepository by lazy {
        MoviesRepositoryImpl(ServerGatewayServiceLocator.moviesAPI, ResponseToSingleMapper())
    }
}