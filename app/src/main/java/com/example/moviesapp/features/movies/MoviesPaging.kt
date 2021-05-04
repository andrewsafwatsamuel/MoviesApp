package com.example.moviesapp.features.movies

import androidx.paging.*
import com.example.MovieResponse
import com.example.moviesapp.createDataSource
import com.example.moviesapp.createPager

fun MovieResponse.createMoviePager(
    size: Int,
    getResponse: (Int) -> Unit
) = createPager(size, createMovieDataSource(size, getResponse)).flow

private fun MovieResponse.createMovieDataSource(
    pageSize: Int,
    loadPage: (Int) -> Unit
) = createDataSource(pageSize) { params, _ ->
    val nextPage = if (results.isNullOrEmpty()) null else pageNumber + 1
    val previousPage = if (pageNumber == 1) null else pageNumber - 1
    loadPage(params.key?:1)
    PagingSource.LoadResult.Page(results, previousPage, nextPage)
}