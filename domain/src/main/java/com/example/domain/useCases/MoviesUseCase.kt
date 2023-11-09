package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.data_sources.DataSourcesServiceLocator
import com.example.data_sources.repositories.MoviesRepository

class MoviesUseCase(private val repository: MoviesRepository = DataSourcesServiceLocator.moviesRepository) {
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
