package com.example.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.domain.FakeMoviesRepository
import com.example.domain.FakeMoviesRepositoryError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class MoviesUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var useCase: MoviesUseCase
    private var isConnected = true
    private val state = MutableLiveData<MovieState>()
    private val params = MovieParams("", 1,"")
    private val context = TestCoroutineDispatcher()

    @Before
    fun initialize() {
        useCase = MoviesUseCase(FakeMoviesRepository())
        state.value = null
        isConnected = true
    }

    @Test
    fun `invoke with successful conditions emits success state`() = runBlockingTest {
        //act
        useCase(isConnected, params, state, context)

        //assert
        assert(state.value is Success)
    }


    @Test
    fun `invoke when not connected flow will not continue and no states will be emitted`() = runBlockingTest {
        //arrange
        isConnected = false

        //act
        useCase(isConnected, params, state, context)

        //assert
        assert(state.value == null)
    }

    @Test
    fun `invoke with loading state flow will not continue and state remains loading`() = runBlockingTest {
        //arrange
        state.value = Loading("")

        //act
        useCase(isConnected, params, state, context)

        //assert
        assert(state.value is Loading)
    }

    @Test
    fun `invoke when error occur then error state emitted`() = runBlockingTest {
        //arrange
        useCase = MoviesUseCase(FakeMoviesRepositoryError())

        //act
        useCase(isConnected, params, state, context)

        //assert
        assert(state.value is Error)
    }


}