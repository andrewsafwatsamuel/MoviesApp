package com.example.moviesapp.features.popularMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.useCases.PopularUseCase
import com.example.moviesapp.subFeatures.movies.QueryParameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularViewModel(
    val movies: MutableList<Movie> = mutableListOf(),
    val result: MutableLiveData<MovieResponse> = MutableLiveData(),
    val loading:MutableLiveData<Boolean> = MutableLiveData(),
    val parameters:MutableLiveData<QueryParameters<Unit>> = MutableLiveData(),
    private val disposable: CompositeDisposable= CompositeDisposable(),
    private val popularUseCase: PopularUseCase= PopularUseCase()
):ViewModel(){
fun getPopularMovies(isConnected:Boolean,pageNumber: Int=1)=
    popularUseCase(isConnected,loading,result,pageNumber)
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribe({},Throwable::printStackTrace)
        ?.also { disposable.addAll() } ?: Unit

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}