package com.example.domain.useCases

import android.util.Log
import com.example.domain.repositories.DetailsRepository
import com.example.domain.repositories.detailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

sealed class DetailsState {
    object Idle:DetailsState()
    object Loading : DetailsState()
    data class Success<T>(val data: T) : DetailsState()
    object Error : DetailsState()
}

class GetDetailsUseCase<T>(private val repository: DetailsRepository = detailsRepository) {
    suspend operator fun invoke(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext = Dispatchers.IO,
        makeRequest: suspend DetailsRepository.(Long)->T
    ) = id
        .takeUnless { states.value is DetailsState.Loading }
        ?.also { states.emit(DetailsState.Loading) }
        ?.let { retrieveDetails(id, states, context,makeRequest) }

    private suspend fun retrieveDetails(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext,
        makeRequest: suspend DetailsRepository.(Long)->T
    ) = try {
        onSuccessfulRetrieve(id, states, context,makeRequest)
    } catch (e: java.lang.Exception) {
        onRetrieveError(e, states)
    }

    private suspend fun onSuccessfulRetrieve(
        id: Long,
        states: MutableStateFlow<DetailsState>,
        context: CoroutineContext,
        makeRequest:suspend DetailsRepository.(Long)->T
    ) = withContext(context) { repository.makeRequest(id) }
        .let(DetailsState::Success)
        .let { states.emit(it) }

    private suspend fun onRetrieveError(e: Exception, states: MutableStateFlow<DetailsState>) {
        Log.e("GetDetailsUseCase", e.message, e)
        states.emit(DetailsState.Error)
    }

}