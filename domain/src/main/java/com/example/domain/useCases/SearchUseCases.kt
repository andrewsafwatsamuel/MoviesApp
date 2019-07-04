package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.SuccessfulQuery
import com.example.domain.repositories.SearchRepository
import com.example.domain.repositories.searchRepository

class MovieSearchUseCase(
    private val repository: SearchRepository = searchRepository
) {
    operator fun invoke(
        connected:Boolean,
        movieName: String,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<MovieResponse>,
        pageNumber: Int
    ) = movieName
        .takeIf { connected }
        ?.takeIf { it.isNotBlank() }
        ?.takeUnless { loading.value ?: false }
        ?.also { loading.postValue(true) }
        ?.let { repository.searchMovie(it, pageNumber) }
        ?.doOnSuccess { result.postValue(it) }
        ?.doFinally { loading.postValue(false) }
}

@Suppress("ReplaceCallWithBinaryOperator")
class StoreMovieNameUseCase(private val repository: SearchRepository = searchRepository) {
    operator fun invoke(movieName: String) {
        movieName
            .takeUnless { checkPresentName(it) }
            ?.let { repository.addSuccessfulQuery(SuccessfulQuery(it)) }
    }

    fun checkPresentName(movieName: String): Boolean {
        return SuccessfulQuery(movieName).equals(repository.checkPresentQuery(movieName))
    }
}

class ShowStoredMoviesUseCase(
    private val repository: SearchRepository = searchRepository
) {
    operator fun invoke(result: MutableLiveData<List<String>>) {
        repository.getSuccessfulQueries()
            .takeUnless { it.isNullOrEmpty() }
            ?.map { it.queryString }
            .also { result.postValue(it ?: listOf()) }
    }
}