package com.moviesapp.data_sources.repositories

import com.moviesapp.Response
import io.reactivex.Single
import io.reactivex.SingleEmitter
import kotlinx.coroutines.runBlocking

class ResponseToSingleMapper {

    operator fun <T> invoke(runner: SuspendResponseRunnable<T>): Single<T> =
        Single.create { emitter ->
            try {
                runBlocking { runner.run() }.emitOnSingleEmitter(emitter)
            } catch (t: Throwable) {
                if (!emitter.isDisposed) emitter.onError(t)
            }
        }

    private fun <T> Response<T>.emitOnSingleEmitter(emitter: SingleEmitter<T>) {
        if (statusCode !in 200..399) throw RuntimeException("Error has occurred: Status code: $statusCode\n$description")
        if (!emitter.isDisposed) body?.let(emitter::onSuccess)
    }

}

fun interface SuspendResponseRunnable<T> {
    suspend fun run(): Response<T>
}
