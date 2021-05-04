package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.domain.repositories.MoviesRepository
import com.example.domain.repositories.moviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

sealed class MovieState
data class Loading(val type: String) : MovieState()
object Error : MovieState()
data class Success(val response: MovieResponse) : MovieState()

data class MovieParams(val category: String, val pageNumber: Int, val loadingType: String)

class MoviesUseCase(private val repository: MoviesRepository = moviesRepository) {

    suspend operator fun invoke(
        isConnected: Boolean,
        params: MovieParams,
        states: MutableLiveData<MovieState>,
        context: CoroutineContext = Dispatchers.IO
    ): Unit? = params
        .takeIf { isConnected }
        ?.takeUnless { states.value is Loading }
        ?.also { states.value = Loading(params.loadingType) }
        ?.makeRequest(context, states)

    private suspend fun MovieParams.makeRequest(
        context: CoroutineContext,
        states: MutableLiveData<MovieState>
    ) = try {
        val response=withContext(context) { repository.getMovies(category, pageNumber) }
        states.value = Success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        states.value = Error
    }
}