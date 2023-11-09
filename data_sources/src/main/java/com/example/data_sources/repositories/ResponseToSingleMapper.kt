package com.example.data_sources.repositories

import com.example.Response
import io.reactivex.Single
import kotlinx.coroutines.runBlocking

class ResponseToSingleMapper {

    inline operator fun <T> invoke(crossinline apiCall: suspend () -> Response<T>): Single<T> =
        Single.create { emitter ->
            try {
                runBlocking { apiCall() }.apply {
                    takeIf { statusCode in 200..399 }
                        ?.body?.let(emitter::onSuccess)
                        ?: throw RuntimeException("Error has occurred: Status code: $statusCode\n$description")
                }
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        }

}
