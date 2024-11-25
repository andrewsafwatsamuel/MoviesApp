package com.moviesapp.presentation.features.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesapp.CreditsResponse
import com.moviesapp.DetailsResponse
import com.moviesapp.Movie
import com.moviesapp.MovieResponse
import com.moviesapp.domain.useCases.DetailsUseCases
import com.moviesapp.presentation.ERROR_MESSAGE
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val disposables: CompositeDisposable = CompositeDisposable(),
    val detailsResult: MutableLiveData<DetailsResponse> = MutableLiveData(),
    val genres: MutableLiveData<List<String>> = MutableLiveData(),
    val relatedResult: MutableLiveData<MovieResponse> = MutableLiveData(),
    val creditsResult: MutableLiveData<CreditsResponse> = MutableLiveData(),
    val movieList:MutableList<Movie> = mutableListOf(),
    val errorLiveData: MutableLiveData<String> = MutableLiveData(),
    private val loadingDetails: MutableLiveData<Boolean> = MutableLiveData(),
    private val loadingCredits: MutableLiveData<Boolean> = MutableLiveData(),
    private val loadingRelated: MutableLiveData<Boolean> = MutableLiveData(),
    private val detailsUseCases: DetailsUseCases = DetailsUseCases()
) : ViewModel() {

    fun setId(id: Long) = detailsUseCases.setId(id)

    fun retrieveDetails(connected: Boolean) = detailsUseCases
        .retrieveDetails(connected, loadingDetails, detailsResult)
        .subscribeSingle()

    fun retrieveCredits(connected: Boolean) = detailsUseCases
        .retrieveCredits(connected, loadingCredits, creditsResult)
        .subscribeSingle()

    fun retrieveRelated(connected: Boolean, pageNumber: Int = 1) = detailsUseCases
        .retrieveRelated(pageNumber, connected, loadingRelated, relatedResult)
        .subscribeSingle()

    private fun <T> Single<T>?.subscribeSingle() = this
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribe({}, {errorLiveData.value=ERROR_MESSAGE})
        ?.also { disposables.add(it) } ?: Unit

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}