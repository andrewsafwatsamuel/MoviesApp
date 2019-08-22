package com.example.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.domain.repositories.BaseMoviesRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single.just
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MoviesUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun `invoke with successful condition then return result`() {
        val repositoryMock = mock<BaseMoviesRepository> {
            on { getMovies("",1) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = MoviesUseCase(repositoryMock)

        popularUseCase(connected, loading,1,""){result.value=it}!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .subscribe()

        assertTrue(result.value != null)
    }

    @Test
    fun `invoke with successful condition then loading value equals false`() {
        val repositoryMock = mock<BaseMoviesRepository> {
            on { getMovies("",1) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = MoviesUseCase(repositoryMock)

        popularUseCase(connected, loading, 1,""){result.value=it}!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .subscribe()

        assertTrue(loading.value == false)
    }

    @Test
    fun `invoke with unsuccessful condition then loading value equals false`() {
        val repositoryMock = mock<BaseMoviesRepository> {
            on { getMovies("",1) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = MoviesUseCase(repositoryMock)

        popularUseCase(connected, loading, 1,""){result.value=it}!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .doOnSubscribe { throw Exception() }
            .subscribe({}, Throwable::printStackTrace)

        assertTrue(loading.value == false)
    }

    @Test
    fun `invoke when is not connected then do not return results`() {
        val repositoryMock = mock<BaseMoviesRepository> {
            on { getMovies("",1) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = false
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = MoviesUseCase(repositoryMock)

        popularUseCase(connected, loading, 1,""){result.value=it}
            ?.observeOn(Schedulers.trampoline())
            ?.subscribeOn(Schedulers.trampoline())
            ?.subscribe()

        assertTrue(result.value == null)
    }


    @Test
    fun `invoke when loading then do not return results`() {
        val repositoryMock = mock<BaseMoviesRepository> {
            on { getMovies("",1) } doReturn   just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = MoviesUseCase(repositoryMock)

        loading.value = true
        popularUseCase(connected, loading, 1,""){result.value=it}
            ?.observeOn(Schedulers.trampoline())
            ?.subscribeOn(Schedulers.trampoline())
            ?.subscribe()

        assertTrue(result.value == null)
    }
}