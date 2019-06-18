package com.example.domain.serverGateWay

import com.example.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val SEARCH_API_END_POINT = "search/movie"
private const val POPULAR_API_END_POINT = "movie/popular"
private const val PAGE_QUERY_PARAMETER = "page"

val movieSearch: SearchApi by lazy {
    RetrofitInstance().create(SearchApi::class.java)
}

interface SearchApi {
    @GET(SEARCH_API_END_POINT)
    fun search(
        @Query("query") movieName: String,
        @Query(PAGE_QUERY_PARAMETER) pageNumber: Int
    ): Single<MovieResponse>
}

val popularMovies: PopularApi by lazy {
    RetrofitInstance().create(PopularApi::class.java)
}

interface PopularApi {
    @GET(POPULAR_API_END_POINT)
    fun getPopularMovies(
        @Query(PAGE_QUERY_PARAMETER) pageNumber: Int
    ): Single<MovieResponse>
}