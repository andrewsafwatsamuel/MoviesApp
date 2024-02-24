package com.moviesapp.domain.engine

import android.app.Application
import androidx.lifecycle.MutableLiveData

internal val applicationLiveData = MutableLiveData<Application>()

internal fun MutableLiveData<Application>.getApplication() = value!!

object Domain {
    operator fun invoke(application: Application) {
        applicationLiveData.value = application
    }
}

fun <T> T.toMutableLiveData() =
    MutableLiveData<T>().also { it.value = this }
