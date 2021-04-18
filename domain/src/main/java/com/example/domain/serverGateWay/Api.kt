package com.example.domain.serverGateWay

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val MOVIE_ID_END_POINT = "movie/{id}"
private const val SEARCH_API_END_POINT = "search/movie"
private const val POPULAR_API_END_POINT = "movie/{category}"
private const val PAGE_QUERY_PARAMETER = "page"


val apiEndPoints: ApiEndPoints by lazy {
    retrofitInstance.create(ApiEndPoints::class.java)
}

interface ApiEndPoints {
    @GET(POPULAR_API_END_POINT)
    fun getMovies(
        @Path("category") category: String,
        @Query(PAGE_QUERY_PARAMETER) pageNumber: Int
    ): Single<MovieResponse>

    @GET(SEARCH_API_END_POINT)
    fun search(
        @Query("query") movieName: String,
        @Query(PAGE_QUERY_PARAMETER) pageNumber: Int
    ): Single<MovieResponse>

    @GET(MOVIE_ID_END_POINT)
    fun retrieveDetails(
        @Path("id") id: Long,
        @Query("append_to_response") append: String = "videos"
    ): Single<DetailsResponse>

    @GET("$MOVIE_ID_END_POINT/credits")
    fun retrieveCredits(
        @Path("id") id: Long
    ): Single<CreditsResponse>

    @GET("$MOVIE_ID_END_POINT/similar")
    fun retrieveRelated(
        @Path("id") id: Long,
        @Query(PAGE_QUERY_PARAMETER) pageNumber: Int
    ): Single<MovieResponse>
}
