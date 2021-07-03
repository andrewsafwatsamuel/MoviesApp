package com.example.domain.useCases

import android.util.Log
import com.example.domain.repositories.DetailsRepository
import com.example.domain.repositories.detailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

sealed class DetailsState {
    data class Success<T>(val data: T) : DetailsState()
    object Error : DetailsState()
    object Loading : DetailsState()
}

class GetDetailsUseCase(private val repository: DetailsRepository = detailsRepository) {
    suspend operator fun invoke(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext = Dispatchers.IO
    ) = id
        .takeUnless { states.value is DetailsState.Loading }
        ?.also { states.emit(DetailsState.Loading) }
        ?.let { retrieveDetails(id, states, context) }

    private suspend fun retrieveDetails(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext
    ) = try {
        onSuccessfulRetrieve(id, states, context)
    } catch (e: java.lang.Exception) {
        onRetrieveError(e, states)
    }

    private suspend fun onSuccessfulRetrieve(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext
    ) = withContext(context) { repository.retrieveDetails(id) }
        .let(DetailsState::Success)
        .let { states.emit(it) }

    private suspend fun onRetrieveError(e: Exception, states: MutableStateFlow<DetailsState>) {
        Log.e("GetDetailsUseCase", e.message, e)
        states.emit(DetailsState.Error)
    }

}

class GetRelatedMoviesUseCase

class GetCreditsUseCase

/*
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
}*/
