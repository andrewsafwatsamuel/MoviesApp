package com.moviesapp.presentation.features.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesapp.Movie
import com.moviesapp.domain.engine.toMutableLiveData
import com.moviesapp.domain.useCases.MoviesUseCase
import com.moviesapp.presentation.ERROR_MESSAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

typealias CategoryList = MutableList<Pair<String, List<Movie>>>

class HomeViewModel(
    private val disposable: CompositeDisposable = CompositeDisposable(),
    private val moviesUseCase: MoviesUseCase = MoviesUseCase(),
    val loadingLiveData: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val errorLiveData: MutableLiveData<String> = MutableLiveData(),
    val result: CategoryList = mutableListOf(),
    val resultLiveData: MutableLiveData<CategoryList> = MutableLiveData()
) : ViewModel() {

    fun loadSingleCategory(isConnected: Boolean, category: String, page: Int = 1) =
        moviesUseCase(isConnected, loadingLiveData, page, category) { addCategory(category, it.results) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({}, { errorLiveData.value = ERROR_MESSAGE})
            ?.let { disposable.add(it) } ?: Unit

    private fun addCategory(category: String, movies: List<Movie>) = result
        .apply { add(category to movies) }
        .also { resultLiveData.postValue(it) }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}