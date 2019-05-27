package com.example.domain.serverGateWay

import com.example.MovieResponse
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_END_POINT="search/movie"

val movieSearch: SearchApi by lazy {
    RetrofitInstance().create(SearchApi::class.java)
}

interface SearchApi{
    @GET(API_END_POINT)
   fun search(
        @Query("query") movieName:String,
        @Query("page") pageNumber:Int,
        @Query("api_key") apiKey:String= API_KEY
    ):Single<MovieResponse>?
}