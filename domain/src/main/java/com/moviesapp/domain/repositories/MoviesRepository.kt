package com.moviesapp.domain.repositories

import com.moviesapp.MovieResponse
import com.moviesapp.domain.serverGateWay.moviesApi
import io.reactivex.Single


interface BaseMoviesRepository{
    fun getMovies(category: String,pageNumber: Int):Single<MovieResponse>
}

val moviesRepository by lazy { MoviesRepository() }
class MoviesRepository:BaseMoviesRepository{
   override fun getMovies(category: String,pageNumber: Int)= moviesApi.getMovies(category,pageNumber)
}