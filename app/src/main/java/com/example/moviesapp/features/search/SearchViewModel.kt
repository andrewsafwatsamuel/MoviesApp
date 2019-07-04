package com.example.moviesapp.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.MovieSearchUseCase
import com.example.domain.useCases.ShowStoredMoviesUseCase
import com.example.moviesapp.subFeatures.movies.QueryParameters
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SearchViewModel(
    val searchPublishSubject :PublishSubject<String> = PublishSubject.create(),
    val storedMovieNames: MutableLiveData<List<String>> = MutableLiveData(),
    val parameterLiveData: MutableLiveData<QueryParameters<String>> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val result: MutableLiveData<MovieResponse> = MutableLiveData(),
    val movieList: MutableList<Movie> = mutableListOf(),
    val disposables: CompositeDisposable = CompositeDisposable(),
    private val movieSearch: MovieSearchUseCase = MovieSearchUseCase(),
    private val storedMovies: ShowStoredMoviesUseCase = ShowStoredMoviesUseCase()
) : ViewModel() {

    fun retrieveMovieNames() = Single
        .fromCallable { storedMovies(storedMovieNames) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({}, Throwable::printStackTrace)
        .also { disposables.add(it) }

    fun retrieveMovies(
        connected: Boolean, movieName: String, pageNumber: Int = 1
    ) {
        movieSearch(connected, movieName, loading, result, pageNumber)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({}, Throwable::printStackTrace)
            ?.also { disposables.add(it) } ?: Unit
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}