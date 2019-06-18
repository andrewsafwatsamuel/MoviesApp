package com.example.domain.repositories

import com.example.MovieResponse
import com.example.domain.serverGateWay.popularMovies
import io.reactivex.Single


interface BasePopularRepository{
    fun getPopularMovies(pageNumber: Int):Single<MovieResponse>
}

val popularRepository by lazy { PopularRepository() }
class PopularRepository:BasePopularRepository{
   override fun getPopularMovies(pageNumber: Int)= popularMovies.getPopularMovies(pageNumber)
}