package com.example.domain

import androidx.lifecycle.MutableLiveData
import com.example.MovieResponse
import com.example.SuccessfulQuery
import com.example.domain.repositories.SearchRepository
import com.example.domain.repositories.searchRepository
import io.reactivex.Single

class MovieSearchUseCase(
    private val loading: MutableLiveData<Boolean>,
    private val repository: SearchRepository = searchRepository
) {
    operator fun invoke(pageNumber: Int, movieName: String): Single<MovieResponse>? {
        return movieName
            .takeIf { it.isNotBlank() }
            ?.takeUnless { loading.value ?: false }
            ?.also { loading.value=true }
            ?.let { repository.searchMovie(it, pageNumber) }
    }
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
    private val result: MutableLiveData<List<String>>,
    private val repository: SearchRepository = searchRepository
) {
    operator fun invoke() {
        repository.getSuccessfulQueries()
            .takeUnless { it.isNullOrEmpty() }
            ?.map { it.queryString }
            .also { result.postValue(it ?: listOf()) }
    }
}