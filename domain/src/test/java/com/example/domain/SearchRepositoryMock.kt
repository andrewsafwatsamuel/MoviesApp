package com.example.domain

import com.example.MovieResponse
import com.example.SuccessfulQuery
import com.example.domain.repositories.SearchRepository
import io.reactivex.Single

val presentQuery = listOf(SuccessfulQuery("lala land"), SuccessfulQuery("inter steller"))

class SearchRepositoryMock : SearchRepository {
    override fun getSuccessfulQueries() = presentQuery

    override fun checkPresentQuery(queryString: String) =
        queryString
            .takeIf { presentQuery.contains(SuccessfulQuery(it)) }
            ?.let { SuccessfulQuery(it) }

    override fun addSuccessfulQuery(query: SuccessfulQuery) = Unit

    override fun searchMovie(movieName: String, pageNumber: Int) = Single.just(MovieResponse(1, 1, 1, listOf()))
}