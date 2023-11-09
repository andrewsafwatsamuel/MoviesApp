package com.example.data_sources.repositories

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.data_sources.server_gateway.api.MoviesAPI
import io.reactivex.Single


interface MoviesRepository {
    fun getMovies(category: String, pageNumber: Int): Single<MovieResponse>
    fun retrieveDetails(id: Long): Single<DetailsResponse>
    fun retrieveCredits(id: Long): Single<CreditsResponse>
    fun retrieveRelated(id: Long, page: Int): Single<MovieResponse>
    fun searchMovie(movieName: String, pageNumber: Int): Single<MovieResponse>
}

class MoviesRepositoryImpl(
    private val moviesAPI: MoviesAPI,
    private val responseToSingleMapper: ResponseToSingleMapper
) : MoviesRepository {

    override fun getMovies(category: String, pageNumber: Int): Single<MovieResponse> =
        responseToSingleMapper { moviesAPI.getMoviesForACategory(category, pageNumber) }

    override fun retrieveDetails(id: Long): Single<DetailsResponse> =
        responseToSingleMapper { moviesAPI.getMovieDetails(id) }

    override fun retrieveCredits(id: Long): Single<CreditsResponse> =
        responseToSingleMapper { moviesAPI.getMovieCredits(id) }

    override fun retrieveRelated(id: Long, page: Int): Single<MovieResponse> =
        responseToSingleMapper { moviesAPI.getRelatedMovies(id, page) }

    override fun searchMovie(movieName: String, pageNumber: Int): Single<MovieResponse> =
        responseToSingleMapper { moviesAPI.search(movieName, pageNumber) }
}