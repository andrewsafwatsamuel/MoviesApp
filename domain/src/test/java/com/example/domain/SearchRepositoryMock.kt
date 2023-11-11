package com.example.domain

import com.example.SuccessfulQuery
import com.example.domain.repositories.SearchRepository

val presentQuery = listOf(SuccessfulQuery("lala land"), SuccessfulQuery("inter steller"))

class SearchRepositoryMock : SearchRepository {
    override fun getSuccessfulQueries() = presentQuery

    override fun checkPresentQuery(queryString: String) =
        queryString
            .takeIf { presentQuery.contains(SuccessfulQuery(it)) }
            ?.let { SuccessfulQuery(it) }

    override fun addSuccessfulQuery(query: SuccessfulQuery) = Unit
}