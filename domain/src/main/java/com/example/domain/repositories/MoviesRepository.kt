package com.example.domain.repositories

import com.example.MovieResponse
import com.example.domain.serverGateWay.apiEndPoints

interface MoviesRepository {
    suspend fun getMovies(category: String, pageNumber: Int): MovieResponse
}

val moviesRepository by lazy { DefaultMoviesRepository() }

class  DefaultMoviesRepository:MoviesRepository {
    override suspend fun getMovies(category: String, pageNumber: Int) =
        apiEndPoints.getMovies(category, pageNumber)
}