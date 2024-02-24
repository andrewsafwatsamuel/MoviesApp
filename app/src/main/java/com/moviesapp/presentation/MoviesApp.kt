package com.moviesapp.presentation

import android.app.Application
import com.moviesapp.domain.engine.Domain

class MoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Domain(this)
    }
}