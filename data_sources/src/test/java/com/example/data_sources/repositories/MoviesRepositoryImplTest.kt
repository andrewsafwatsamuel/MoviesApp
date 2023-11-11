package com.example.data_sources.repositories

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.data_sources.server_gateway.api.MoviesAPI
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MoviesRepositoryImplTest {
    private lateinit var moviesRepository: MoviesRepositoryImpl
    private lateinit var moviesAPI: MoviesAPI
    private lateinit var responseToSingleMapper: ResponseToSingleMapper

    @BeforeEach
    fun setUp() {
        moviesAPI = mockk(relaxed = true)
        responseToSingleMapper = mockk(relaxed = true)
        moviesRepository = MoviesRepositoryImpl(moviesAPI, responseToSingleMapper)
    }

    @Test
    fun `given getMovies when invoked then moviesAPI_getMoviesForACategory() should be invoked`() {
        // arrange
        mockInvoke<MovieResponse>()

        // act
        moviesRepository.getMovies("category", 1)

        // assert
        coVerify(exactly = 1) { moviesAPI.getMoviesForACategory("category", 1) }
    }

    @Test
    fun `given retrieveDetails when invoked then moviesAPI_getMovieDetails() should be invoked`() {
        // arrange
        mockInvoke<DetailsResponse>()

        // act
        moviesRepository.retrieveDetails(1)

        // assert
        coVerify(exactly = 1) { moviesAPI.getMovieDetails(1) }
    }

    @Test
    fun `given retrieveCredits when invoked then moviesAPI_getMovieCredits() should be invoked`() {
        // arrange
        mockInvoke<CreditsResponse>()

        // act
        moviesRepository.retrieveCredits(1)

        // assert
        coVerify(exactly = 1) { moviesAPI.getMovieCredits(1) }
    }

    @Test
    fun `given retrieveRelated when invoked then moviesAPI_getRelatedMovies() should be invoked`() {
        // arrange
        mockInvoke<MovieResponse>()

        // act
        moviesRepository.retrieveRelated(1, 1)

        // assert
        coVerify(exactly = 1) { moviesAPI.getRelatedMovies(1, 1) }
    }

    @Test
    fun `given searchMovie when invoked then moviesAPI_search() should be invoked`() {
        // arrange
        mockInvoke<MovieResponse>()

        // act
        moviesRepository.searchMovie("movie", 1)

        // assert
        coVerify(exactly = 1) { moviesAPI.search("movie", 1) }
    }

    private fun <T> mockInvoke() = coEvery { responseToSingleMapper.invoke<T>(any()) } coAnswers {
        Single.just(firstArg<SuspendResponseRunner<T>>().run().body)
    }
}