package com.example.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.SuccessfulQuery
import com.example.domain.repositories.SearchRepository
import com.example.domain.repositories.searchRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ShowStoredMoviesUseCaseTest{

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `invoke with successful conditions then return list of movie Names`(){
         val result= MutableLiveData<List<String>>()
         val repositoryMock= mock<SearchRepository> {
             on { getSuccessfulQueries() } doReturn presentQuery
         }

        with(ShowStoredMoviesUseCase(result,repositoryMock)) {
            invoke()
        }

        assertFalse(result.value.isNullOrEmpty())
    }

    @Test
    fun `invoke with empty successful queries return emptyList`(){
        val result= MutableLiveData<List<String>>()
        val repositoryMock= mock<SearchRepository> {
            on { getSuccessfulQueries() } doReturn  listOf(null)
        }

        with(ShowStoredMoviesUseCase(result,repositoryMock)) {
            invoke()
        }

        assertTrue(result.value!=null)
    }

}