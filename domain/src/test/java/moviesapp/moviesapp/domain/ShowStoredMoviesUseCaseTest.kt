package moviesapp.moviesapp.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.moviesapp.SuccessfulQuery
import com.moviesapp.domain.repositories.SearchRepository
import com.moviesapp.domain.useCases.ShowStoredMoviesUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ShowStoredMoviesUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `invoke with successful conditions then return list of movie Names`() {
        val result = MutableLiveData<List<String>>()
        val repositoryMock = mock<SearchRepository> {
            on { getSuccessfulQueries() } doReturn listOf(
                SuccessfulQuery("lala land")
            )
        }

        with(ShowStoredMoviesUseCase(repositoryMock)) {
            invoke(result)
        }

        assertFalse(result.value.isNullOrEmpty())
    }

    @Test
    fun `invoke with empty successful queries return emptyList`() {
        val result = MutableLiveData<List<String>>()
        val repositoryMock = mock<SearchRepository> {
            on { getSuccessfulQueries() } doReturn listOf(null)
        }

        with(ShowStoredMoviesUseCase(repositoryMock)) {
            invoke(result)
        }

        assertTrue(result.value != null)
    }

}