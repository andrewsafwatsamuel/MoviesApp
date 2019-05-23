package com.example.moviesapp

import android.app.Application
import com.example.domain.engine.Domain

class MoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Domain(this)
    }
}