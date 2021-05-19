package com.example.domain.useCases

import androidx.paging.PagingSource.LoadResult
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.repositories.MoviesRepository
import com.example.domain.repositories.moviesRepository

class MoviesUseCase(private val repository: MoviesRepository = moviesRepository) {

    suspend operator fun invoke(category: String, key: Int): LoadResult<Int, Movie> = try {
        getPage(category, key)
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    private suspend fun getPage(category: String, currentPage: Int) = repository
        .getMovies(category, currentPage)
        .getPagedMovieResponse()

    private fun MovieResponse.getPagedMovieResponse() = LoadResult.Page(
        results,
        getPreviousKey(pageNumber),
        getNextKey(pageNumber, pageCount)
    )

    private fun getNextKey(pageNumber: Int, pageCount: Int) =
        if (pageNumber < pageCount) pageNumber + 1 else null

    private fun getPreviousKey(pageNumber: Int) = if (pageNumber == 1) null else pageNumber - 1

}