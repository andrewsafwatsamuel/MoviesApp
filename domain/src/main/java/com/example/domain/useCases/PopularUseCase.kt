package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.domain.repositories.BasePopularRepository
import com.example.domain.repositories.popularRepository

class PopularUseCase(private val repository: BasePopularRepository = popularRepository) {
    operator fun invoke(
        isConnected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<MovieResponse>,
        pageNumber: Int = 1
    ) = repository.getPopularMovies(pageNumber)
        .takeIf { isConnected }
        ?.takeUnless { loading.value ?: false }
        ?.also { loading.postValue(true) }
        ?.doOnSuccess { result.value = it }
        ?.doFinally { loading.value = false }
}