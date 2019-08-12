package com.example.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.domain.repositories.DetailsRepository
import com.example.domain.repositories.detailsRepository

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

    fun retrieveDetails(
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<DetailsResponse>
    ) = checkConditions(connected, loading)
            .also { loading.postValue(true) }
            ?.let { repository.retrieveDetails(it) }
            ?.doOnSuccess { result.postValue(it) }
            ?.doFinally { loading.postValue(false )}

    fun retrieveCredits(
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<CreditsResponse>
    ) = checkConditions(connected, loading)
        .also { loading.postValue(true) }
        ?.let { repository.retrieveCredits(it) }
        ?.doOnSuccess { result.postValue(it) }
        ?.doFinally { loading.postValue(false )}


    fun retrieveRelated(
        pageNumber : Int,
        connected: Boolean,
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<MovieResponse>
    ) = checkConditions(connected, loading)
        .also { loading.postValue(true) }
        ?.let { repository.retrieveRelated(it,pageNumber) }
        ?.doOnSuccess { result.postValue(it) }
        ?.doFinally { loading.postValue(false )}
}