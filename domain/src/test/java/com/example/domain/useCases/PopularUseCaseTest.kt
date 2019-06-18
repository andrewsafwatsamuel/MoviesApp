package com.example.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.repositories.BasePopularRepository
import com.example.domain.repositories.DetailsRepository
import com.example.domain.repositories.PopularRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.schedulers.Schedulers
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.Exception

//if is not connected then do not retrieve data
//if is retrieving do not start retrieving
//if all is ok then retrieve
class PopularUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun `invoke with successful condition then return results`() {
        val repositoryMock = mock<BasePopularRepository> {
            on { getPopularMovies(1) } doReturn Single.just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = PopularUseCase(repositoryMock)

        popularUseCase(connected, loading, result)!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .subscribe()

        assertTrue(result.value != null)
    }

    @Test
    fun `invoke with successful condition then loading value equals false`() {
        val repositoryMock = mock<BasePopularRepository> {
            on { getPopularMovies(1) } doReturn Single.just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = PopularUseCase(repositoryMock)

        popularUseCase(connected, loading, result)!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .subscribe()

        assertTrue(loading.value == false)
    }

    @Test
    fun `invoke with unsuccessful condition then loading value equals false`() {
        val repositoryMock = mock<BasePopularRepository> {
            on { getPopularMovies(1) } doReturn Single.just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = PopularUseCase(repositoryMock)

        popularUseCase(connected, loading, result)!!
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
            .doOnSubscribe { throw Exception() }
            .subscribe({}, Throwable::printStackTrace)

        assertTrue(loading.value == false)
    }

    @Test
    fun `invoke when is not connected then do not return results`() {
        val repositoryMock = mock<BasePopularRepository> {
            on { getPopularMovies(1) } doReturn Single.just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = false
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = PopularUseCase(repositoryMock)

        popularUseCase(connected, loading, result)
            ?.observeOn(Schedulers.trampoline())
            ?.subscribeOn(Schedulers.trampoline())
            ?.subscribe()

        assertTrue(result.value == null)
    }


    @Test
    fun `invoke when loading then do not return results`() {
        val repositoryMock = mock<BasePopularRepository> {
            on { getPopularMovies(1) } doReturn Single.just(
                MovieResponse(1, 1, 1, listOf())
            )
        }
        val loading = MutableLiveData<Boolean>()
        val connected = true
        val result = MutableLiveData<MovieResponse>()
        val popularUseCase = PopularUseCase(repositoryMock)

        loading.value = true
        popularUseCase(connected, loading, result)
            ?.observeOn(Schedulers.trampoline())
            ?.subscribeOn(Schedulers.trampoline())
            ?.subscribe()

        assertTrue(result.value == null)
    }
}