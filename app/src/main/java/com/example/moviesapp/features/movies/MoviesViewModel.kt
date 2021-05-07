package com.example.moviesapp.features.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.MovieParams
import com.example.domain.useCases.MovieState
import com.example.domain.useCases.MoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

typealias MoviePagingPair = Pair<Int, List<Movie>>

class MoviesViewModel(
    params: MovieParams,
    isConnected: Boolean,
    private val useCase: MoviesUseCase = MoviesUseCase(),
    private val mutablePagingFlow: MutableStateFlow<MoviePagingPair> = MutableStateFlow(Pair(-1, listOf())),
    val state: MutableLiveData<MovieState> = MutableLiveData(),
    val pagingFlow:StateFlow<MoviePagingPair> = mutablePagingFlow.asStateFlow()
) : ViewModel() {

    init {
        getMovies(isConnected, params)
    }

    fun getMovies(isConnected: Boolean, params: MovieParams) =
        viewModelScope.launch(Dispatchers.Main) { useCase(isConnected, params, state) }

    suspend fun submitPage(response: MovieResponse) = mutablePagingFlow.value.second
        .plus(response.results)
        .distinct()
        .let { emitPagingValue(response.pageNumber, it) }

    private suspend fun emitPagingValue(
        pageNumber: Int,
        value: List<Movie>
    ) = Pair(pageNumber + 1, value)
        .let { mutablePagingFlow.emit(it) }

}

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(
    private val params: MovieParams,
    private val isConnected: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) MoviesViewModel(
            params,
            isConnected
        ) as T
        else throw IllegalStateException("Bad ViewModel class")
}