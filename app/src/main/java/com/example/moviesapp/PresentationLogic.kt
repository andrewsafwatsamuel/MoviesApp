package com.example.moviesapp

import android.widget.ImageView
import com.bumptech.glide.Glide

const val BASE_POSTER_URL = "http://image.tmdb.org/t/p/"
const val POSTER_SIZE="w185"
const val BACK_DRAW_SIZE="w500"

fun drawPhoto(size: String, url: String, view: ImageView) = url
    .takeUnless { it.isBlank() }
    ?.also { Glide.with(view).load("$BASE_POSTER_URL$size$it").into(view) }