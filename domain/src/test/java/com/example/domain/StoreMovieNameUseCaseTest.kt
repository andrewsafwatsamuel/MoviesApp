package com.example.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.useCases.StoreMovieNameUseCase
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class StoreMovieNameUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //if movieName is stored return true
    @Test
    fun `checkPresentName with movie name in SuccessfulQueries then return true`() {
        val searchRepositoryMock = SearchRepositoryMock()
        val movieName = "lala land"
        val storeMovieNameUseCase = StoreMovieNameUseCase(searchRepositoryMock)
        val result = storeMovieNameUseCase.checkPresentName(movieName)

        assertTrue(result)
    }

    //if the movie is not stored
    @Test
    fun `checkPresentName with movie name isn't in SuccessfulQueries then return false`() {
        val searchRepositoryMock = SearchRepositoryMock()
        val movieName = "lala "
        val storeMovieNameUseCase = StoreMovieNameUseCase(searchRepositoryMock)
        val result = storeMovieNameUseCase.checkPresentName(movieName)

        assertFalse(result)
    }
}