package com.example.domain.repositories

import com.example.MovieResponse
import com.example.domain.serverGateWay.apiEndPoints
import io.reactivex.Single


interface BaseMoviesRepository{
    fun getMovies(category: String,pageNumber: Int):Single<MovieResponse>
}

val moviesRepository by lazy { MoviesRepository() }
class MoviesRepository:BaseMoviesRepository{
   override fun getMovies(category: String,pageNumber: Int)= apiEndPoints.getMovies(category,pageNumber)
}