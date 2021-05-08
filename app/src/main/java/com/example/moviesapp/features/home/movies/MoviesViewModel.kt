package com.example.moviesapp.features.home.movies

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

const val INIT_LOADING = "initial_loading"
const val PAGED_LOADING = "paged_loading"
const val REFRESH_LOADING = "refresh_loading"

class MoviesViewModel(
    isConnected: Boolean,
    private val category: String,
    private val useCase: MoviesUseCase = MoviesUseCase(),
    private val mutablePagingFlow: MutableStateFlow<MoviePagingPair> = MutableStateFlow(Pair(1, listOf())),
    val state: MutableLiveData<MovieState> = MutableLiveData(),
    val pagingFlow: StateFlow<MoviePagingPair> = mutablePagingFlow.asStateFlow()
) : ViewModel() {

    init {
        getMovies(isConnected, createParams(INIT_LOADING))
    }

    fun createParams(
        loadingType: String,
        pageNumber: Int = pagingFlow.value.first
    ) = MovieParams(category, pageNumber, loadingType)

    fun getMovies(isConnected: Boolean, params: MovieParams) =
        viewModelScope.launch(Dispatchers.Main) { useCase(isConnected, params, state) }

    suspend fun submitPage(response: MovieResponse) = response.run {
        emitPagingValue(pageNumber, submitList(this))
    }

    private fun submitList(response: MovieResponse) =
        if (response.pageNumber == 1) response.results
        else mutablePagingFlow.value.second.plus(response.results).distinct()

    private suspend fun emitPagingValue(
        pageNumber: Int,
        value: List<Movie>
    ) = Pair(pageNumber + 1, value)
        .let { mutablePagingFlow.emit(it) }

}

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(
    private val isConnected: Boolean,
    private val category: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) MoviesViewModel(isConnected, category) as T
        else throw IllegalStateException("Bad ViewModel class")
}