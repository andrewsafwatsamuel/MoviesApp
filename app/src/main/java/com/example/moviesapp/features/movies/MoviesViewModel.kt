package com.example.moviesapp.features.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.useCases.MovieParams
import com.example.domain.useCases.MovieState
import com.example.domain.useCases.MoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class MoviesViewModel(
    params: MovieParams,
    isConnected: Boolean,
    private val useCase: MoviesUseCase = MoviesUseCase(),
    val state: MutableLiveData<MovieState> = MutableLiveData()
) : ViewModel() {

    init {
        getMovies(isConnected, params)
    }

    fun getMovies(isConnected: Boolean, params: MovieParams) =
        viewModelScope.launch(Dispatchers.Main) { useCase(isConnected, params, state) }

}

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(
    private val params: MovieParams,
    private val isConnected: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) MoviesViewModel(params, isConnected) as T
        else throw IllegalStateException("Bad ViewModel class")
}