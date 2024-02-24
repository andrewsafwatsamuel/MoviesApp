package com.moviesapp.presentation.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesapp.Movie
import com.moviesapp.MovieResponse
import com.moviesapp.domain.useCases.MovieSearchUseCase
import com.moviesapp.domain.useCases.ShowStoredMoviesUseCase
import com.moviesapp.presentation.ERROR_MESSAGE
import com.moviesapp.presentation.subFeatures.movies.QueryParameters
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SearchViewModel(
    val storedMovieNames: MutableLiveData<List<String>> = MutableLiveData(),
    val parameterLiveData: MutableLiveData<QueryParameters<String>> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val result: MutableLiveData<MovieResponse> = MutableLiveData(),
    val movieList: MutableList<Movie> = mutableListOf(),
    val disposables: CompositeDisposable = CompositeDisposable(),
    val errorLiveData: MutableLiveData<String> = MutableLiveData(),
    val searchSubject:BehaviorSubject<CharSequence> = BehaviorSubject.create(),
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
            ?.subscribe({errorLiveData.value=null}, {errorLiveData.value= ERROR_MESSAGE})
            ?.also { disposables.add(it) } ?: Unit
    }

    fun updateParametersLiveData(parameters:QueryParameters<String>){
        parameterLiveData.value=parameters
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}