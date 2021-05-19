package com.example.domain

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.domain.repositories.SearchRepository
import com.example.domain.useCases.MovieSearchUseCase
import io.reactivex.Single.just
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MovieSearchUseCaseTest {

    /*@get:Rule
    //val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `invoke with successful conditions result is not null`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = mock<SearchRepository> {
            on { searchMovie(movieName, pageNumber) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()
        val movieSearchUseCase = MovieSearchUseCase(mockRepository)

        movieSearchUseCase(true,movieName, loading, result, pageNumber)
            ?.subscribeOn(Schedulers.trampoline())
            ?.observeOn(Schedulers.trampoline())
            ?.subscribe({}, Throwable::printStackTrace)

        assert(result.value != null)
    }

    @Test
    fun `invoke with blank movie name return no results`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = mock<SearchRepository> {
            on { searchMovie(movieName, pageNumber) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()
        val movieSearchUseCase = MovieSearchUseCase(mockRepository)

        movieSearchUseCase(true,"", loading, result, pageNumber)
            ?.subscribeOn(Schedulers.trampoline())
            ?.observeOn(Schedulers.trampoline())
            ?.subscribe({}, Throwable::printStackTrace)

        assert(result.value == null)
    }

    @Test
    fun `invoke while loading then return no results`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = mock<SearchRepository> {
            on { searchMovie(movieName, pageNumber) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()
        val movieSearchUseCase = MovieSearchUseCase(mockRepository)

        loading.value = true
        movieSearchUseCase(true,movieName, loading, result, pageNumber)
            ?.subscribeOn(Schedulers.trampoline())
            ?.observeOn(Schedulers.trampoline())
            ?.subscribe({}, Throwable::printStackTrace)

        assert(result.value == null)
    }

    @Test
    fun `invoke with successful response then loading value equals false`() {
        val pageNumber = 1
        val movieName = "lala land"
        val mockRepository = mock<SearchRepository> {
            on { searchMovie(movieName, pageNumber) } doReturn just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()
        val movieSearchUseCase = MovieSearchUseCase(mockRepository)

        movieSearchUseCase(true,movieName, loading, result, pageNumber)
            ?.subscribeOn(Schedulers.trampoline())
            ?.observeOn(Schedulers.trampoline())
            ?.subscribe({}, Throwable::printStackTrace)

        assert(loading.value == false)
    }*/
}