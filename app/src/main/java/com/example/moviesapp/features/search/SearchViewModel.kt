package com.example.moviesapp.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.MovieSearchUseCase
import com.example.domain.ShowStoredMoviesUseCase
import com.example.domain.StoreMovieNameUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

const val NOT_CONNECTED = "Please check your internet connection"
private const val NO_RESULTS = "It seems that there are no movies with that name"

class SearchViewModel(
    val disposables: CompositeDisposable = CompositeDisposable(),
    val storedMovieNames: MutableLiveData<List<String>> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val emptyResult: MutableLiveData<String> = MutableLiveData(),
    val movieList: ArrayList<Movie> = ArrayList(),
    private val storedMovies: ShowStoredMoviesUseCase = ShowStoredMoviesUseCase(storedMovieNames),
    val movieSearch: MovieSearchUseCase = MovieSearchUseCase(loading),
    private val storeMovieNameUseCase: StoreMovieNameUseCase = StoreMovieNameUseCase()
) : ViewModel() {

    fun retrieveMovieNames() = Single
        .fromCallable { storedMovies() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        ?.also { disposables.add(it) }

    private fun storeMovieName(movieName: String) {
        Single.fromCallable { storeMovieNameUseCase(movieName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Throwable::printStackTrace)
            .also { disposables.add(it) }
    }

    private val connectionError =
        "Unable to resolve host \"api.themoviedb.org\": No address associated with hostname"

    fun retrieveMovies(
        onSuccess: (MovieResponse) -> Unit, pageNumber: Int, movieName: String
    ) {
        movieSearch(pageNumber, movieName)
            ?.subscribeOn(Schedulers.io())
            ?.also { emptyResult.postValue("") }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSuccess { onSuccess(it) }
            ?.doOnError {
                loading.value = false
                if (it.localizedMessage == connectionError)
                    emptyResult.value = NOT_CONNECTED
            }
            ?.subscribe({ finisLoadingSuccess(it, movieName) }, Throwable::printStackTrace)
            ?.also { disposables.add(it) }
    }


    private fun finisLoadingSuccess(result: MovieResponse, movieName: String) {
        loading.value = false
        if (result.resultCount == 0) emptyResult.value = NO_RESULTS
        else storeMovieName(movieName)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

