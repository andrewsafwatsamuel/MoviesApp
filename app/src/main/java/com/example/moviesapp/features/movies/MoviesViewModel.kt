package com.example.moviesapp.features.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.MoviesUseCase
import com.example.moviesapp.subFeatures.movies.QueryParameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesViewModel(
    val movies: MutableList<Movie> = mutableListOf(),
    val result: MutableLiveData<MovieResponse> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val parameters: MutableLiveData<QueryParameters<Unit>> = MutableLiveData(),
    val disposable: CompositeDisposable = CompositeDisposable(),
    private val popularUseCase: MoviesUseCase = MoviesUseCase()
) : ViewModel() {
    fun getMovies(isConnected: Boolean, category: String, pageNumber: Int = 1) =
        popularUseCase(isConnected, loading, pageNumber,category) { result.postValue(it)}
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({}, Throwable::printStackTrace)
            ?.also { disposable.add(it) } ?: Unit

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}