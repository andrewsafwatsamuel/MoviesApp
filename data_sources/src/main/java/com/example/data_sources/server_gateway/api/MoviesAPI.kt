package com.example.data_sources.server_gateway.api

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.Response
import com.example.data_sources.server_gateway.toResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

interface MoviesAPI {
    suspend fun search(searchQuery: String, pageNumber: Int): Response<MovieResponse>
    suspend fun getMoviesForACategory(category: String, pageNumber: Int): Response<MovieResponse>
    suspend fun getMovieDetails(movieId: Long): Response<DetailsResponse>
    suspend fun getMovieCredits(movieId: Long): Response<CreditsResponse>
    suspend fun getRelatedMovies(movieId: Long, pageNumber: Int): Response<MovieResponse>
}

class MoviesAPIImpl(private val httpClient: HttpClient) : MoviesAPI {

    override suspend fun search(searchQuery: String, pageNumber: Int): Response<MovieResponse> =
        httpClient.get(SEARCH_API_END_POINT) {
            url.parameters.apply {
                append(QUERY_KEY, searchQuery)
                append(PAGE_KEY, pageNumber.toString())
            }
        }.toResponse()

    override suspend fun getMoviesForACategory(
        category: String,
        pageNumber: Int
    ): Response<MovieResponse> = httpClient
        .get("$GET_MOVIES_API$category") { url.parameters.append(PAGE_KEY, pageNumber.toString()) }
        .toResponse()

    override suspend fun getMovieDetails(movieId: Long): Response<DetailsResponse> = httpClient
        .get(GET_MOVIE_DETAILS.format(movieId)) {
            url.parameters.append(APPEND_TO_RESPONSE_KEY, VIDEOS)
        }.toResponse()

    override suspend fun getMovieCredits(movieId: Long): Response<CreditsResponse> =
        httpClient.get(CREDITS_ENDPOINT.format(movieId)).toResponse()

    override suspend fun getRelatedMovies(movieId: Long, pageNumber: Int): Response<MovieResponse> =
        httpClient.get(RELATED_MOVIES_ENDPOINT.format(movieId)) {
            url.parameters.append(PAGE_KEY, pageNumber.toString())
        }.toResponse()

    companion object {
        // Endpoints
        private const val SEARCH_API_END_POINT = "search/movie"
        private const val GET_MOVIES_API = "movie/"
        private const val GET_MOVIE_DETAILS = "$GET_MOVIES_API%s"
        private const val CREDITS_ENDPOINT = "$GET_MOVIE_DETAILS/credits"
        private const val RELATED_MOVIES_ENDPOINT = "$GET_MOVIE_DETAILS/similar"

        // Query parameters
        private const val PAGE_KEY = "page"
        private const val QUERY_KEY = "query"
        private const val APPEND_TO_RESPONSE_KEY = "append_to_response"

        // Values
        private const val VIDEOS = "videos"
    }
}