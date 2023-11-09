package com.example.data_sources.repositories

import com.example.Response
import io.reactivex.Single
import io.reactivex.SingleEmitter
import kotlinx.coroutines.runBlocking

class ResponseToSingleMapper {

    inline operator fun <T> invoke(crossinline apiCall: suspend () -> Response<T>): Single<T> =
        Single.create { emitter ->
            try {
                runBlocking { apiCall() }.emitOnSingleEmitter(emitter)
            } catch (t: Throwable) {
                if (!emitter.isDisposed) emitter.onError(t)
            }
        }

    fun <T> Response<T>.emitOnSingleEmitter(emitter: SingleEmitter<T>) {
        if (statusCode !in 200..399) throw RuntimeException("Error has occurred: Status code: $statusCode\n$description")
        if (!emitter.isDisposed) body?.let(emitter::onSuccess)
    }

}
