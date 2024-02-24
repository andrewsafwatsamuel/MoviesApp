package com.moviesapp.presentation.features.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesapp.Movie
import com.moviesapp.MovieResponse
import com.moviesapp.domain.useCases.MoviesUseCase
import com.moviesapp.presentation.ERROR_MESSAGE
import com.moviesapp.presentation.subFeatures.movies.QueryParameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesViewModel(
    val movies: MutableList<Movie> = mutableListOf(),
    val result: MutableLiveData<MovieResponse> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val parameters: MutableLiveData<QueryParameters<Unit>> = MutableLiveData(),
    val disposable: CompositeDisposable = CompositeDisposable(),
    val errorLiveData: MutableLiveData<String> = MutableLiveData(),
    private val popularUseCase: MoviesUseCase = MoviesUseCase()
) : ViewModel() {
    fun getMovies(isConnected: Boolean, category: String, pageNumber: Int = 1) =
        popularUseCase(isConnected, loading, pageNumber,category) { result.postValue(it)}
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({errorLiveData.value=null},{errorLiveData.value= ERROR_MESSAGE})
            ?.also { disposable.add(it) } ?: Unit

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}