package com.example.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MovieSearchUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //if all is ok start searching
    @Test
    fun `invoke with successful conditions return single object containing response`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = SearchRepositoryMock()
        val loading = MutableLiveData<Boolean>()

        val movieSearchUseCase = MovieSearchUseCase(loading, mockRepository)
        val result = movieSearchUseCase(pageNumber, movieName)

        assertFalse(result == null)
    }

    //if movie name is blank do not start search
    @Test
    fun `invoke with empty Movie name then do not return results`() {
        val pageNumber = 1
        val movieName = ""
        val mockRepository = SearchRepositoryMock()
        val loading = MutableLiveData<Boolean>()

        val movieSearchUseCase = MovieSearchUseCase(loading, mockRepository)
        val result = movieSearchUseCase(pageNumber, movieName)

        assertTrue(result == null)
    }

    //if is searching do not start search
    @Test
    fun `invoke while searching then do not return result`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = SearchRepositoryMock()
        val loading = MutableLiveData<Boolean>()

        loading.value = true
        val movieSearchUseCase = MovieSearchUseCase(loading, mockRepository)
        val result = movieSearchUseCase(pageNumber, movieName)

        assertTrue(result == null)
    }

}