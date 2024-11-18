package com.moviesapp.domain.repositories

import com.moviesapp.SuccessfulQuery
import com.moviesapp.domain.databaseGateWay.movieAppDatabase

interface SearchRepository {
    fun getSuccessfulQueries(): List<SuccessfulQuery>?
    fun checkPresentQuery(queryString: String): SuccessfulQuery?
    fun addSuccessfulQuery(query: SuccessfulQuery)
}

val searchRepository:SearchRepositoryImplementer by lazy {SearchRepositoryImplementer()}

class SearchRepositoryImplementer : SearchRepository {
    override fun getSuccessfulQueries() = movieAppDatabase.successfulQueryDao.queryAll()

    override fun checkPresentQuery(queryString: String) =
        movieAppDatabase.successfulQueryDao.checkPresentQuery(queryString)

    override fun addSuccessfulQuery(query: SuccessfulQuery) = movieAppDatabase.successfulQueryDao.addQuery(query)

}