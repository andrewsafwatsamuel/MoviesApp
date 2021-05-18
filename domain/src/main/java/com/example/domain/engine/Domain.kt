package com.example.domain.engine

import android.content.Context

object Domain {

    lateinit var applicationContext: Context
        private set

    operator fun invoke(context: Context) {
        applicationContext = context
    }
}