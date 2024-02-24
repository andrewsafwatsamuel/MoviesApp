package com.moviesapp.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.moviesapp.MovieResponse
import com.moviesapp.domain.repositories.BaseMoviesRepository
import com.moviesapp.domain.repositories.moviesRepository

class MoviesUseCase(private val repository: BaseMoviesRepository = moviesRepository) {
    operator fun invoke(
        isConnected: Boolean,
        loading: MutableLiveData<Boolean>,
        pageNumber: Int,
        category: String,
        result: (MovieResponse) -> Unit
    ) = repository.getMovies(category, pageNumber)
        .takeIf { isConnected }
        ?.takeUnless { loading.value ?: false }
        ?.also { loading.postValue(true) }
        ?.doOnSuccess { result(it) }
        ?.doFinally { loading.postValue(false) }
}
