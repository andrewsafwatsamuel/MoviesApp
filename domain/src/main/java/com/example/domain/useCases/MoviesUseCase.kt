package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.domain.repositories.BaseMoviesRepository
import com.example.domain.repositories.moviesRepository

class MoviesUseCase(private val repository: BaseMoviesRepository = moviesRepository) {
    operator fun invoke(
        isConnected: Boolean,
        loading: MutableLiveData<Boolean>,
        pageNumber: Int,
        category: String = "popular",
        result: (MovieResponse) -> Unit
    ) = repository.getMovies(category, pageNumber)
        .takeIf { isConnected }
        ?.takeUnless { loading.value ?: false }
        ?.also { loading.postValue(true) }
        ?.doOnSuccess { result(it) }
        ?.doFinally { loading.postValue(false) }
}
