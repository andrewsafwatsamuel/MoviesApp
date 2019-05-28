package com.example.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.repositories.DetailsRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DetailsUseCasesKtTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `retrieveGenres in successful conditions then retrieve genre list`() {
        val ids = listOf(1, 2, 3)
        val repositoryMock = mock<DetailsRepository> {
            on { retrieveGenres(ids) } doReturn mapOf(1 to "1", 2 to "2", 3 to "3").map { it.value }
        }
        with(
            retrieveGenres(ids, repositoryMock)
        ) {
            assertTrue(this.size == 3)
        }
    }

    @Test
    fun `retrieveGenres with empty id list then retrieve nothing`() {
        val ids = listOf<Int>()
        val repositoryMock = mock<DetailsRepository> {
            on { retrieveGenres(ids) } doReturn mapOf(1 to "1", 2 to "2", 3 to "3").map { it.value }
        }
        with(
            retrieveGenres(ids, repositoryMock)
        ) {
            assertTrue(this.isEmpty())
        }
    }
}