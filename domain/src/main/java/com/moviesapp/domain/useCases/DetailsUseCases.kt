package com.moviesapp.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.moviesapp.CreditsResponse
import com.moviesapp.DetailsResponse
import com.moviesapp.MovieResponse
import com.moviesapp.domain.repositories.DetailsRepository
import com.moviesapp.domain.repositories.detailsRepository

class DetailsUseCases(
    private val repository: DetailsRepository = detailsRepository,
    private var id: Long = 0
) {

    fun setId(id: Long) {
        this.id = id
    }

    private fun checkConditions(
        connected: Boolean,
        loading: MutableLiveData<Boolean>
    ) = id.also { if (it == 0L) throw IllegalStateException("Id must have value") }
        .takeIf { connected }
        .takeUnless { loading.value ?: false }
        .also { if (connected) loading.postValue(true) }

    fun retrieveDetails(
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<DetailsResponse>
    ) = checkConditions(connected, loading)
            ?.let { repository.retrieveDetails(it) }
            ?.doOnSuccess { result.postValue(it) }
            ?.doFinally { loading.postValue(false )}

    fun retrieveCredits(
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<CreditsResponse>
    ) = checkConditions(connected, loading)
        ?.let { repository.retrieveCredits(it) }
        ?.doOnSuccess { result.postValue(it) }
        ?.doFinally { loading.postValue(false )}


    fun retrieveRelated(
        pageNumber : Int,
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<MovieResponse>
    ) = checkConditions(connected, loading)
        ?.let { repository.retrieveRelated(it,pageNumber) }
        ?.doOnSuccess { result.postValue(it) }
        ?.doFinally { loading.postValue(false )}
}