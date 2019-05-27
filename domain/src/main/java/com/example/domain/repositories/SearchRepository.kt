package com.example.domain.repositories

import com.example.MovieResponse
import com.example.SuccessfulQuery
import com.example.domain.databaseGateWay.movieAppDatabase
import com.example.domain.serverGateWay.movieSearch
import io.reactivex.Flowable
import io.reactivex.Single

interface SearchRepository {
    fun getSuccessfulQueries(): List<SuccessfulQuery>?
    fun checkPresentQuery(queryString: String): SuccessfulQuery?
    fun addSuccessfulQuery(query: SuccessfulQuery)
    fun searchMovie( movieName:String,pageNumber:Int):Single<MovieResponse>?
}

val searchRepository:SearchRepositoryImplementer by lazy {SearchRepositoryImplementer()}

class SearchRepositoryImplementer : SearchRepository {
    override fun getSuccessfulQueries() = movieAppDatabase.successfulQueryDao.queryAll()

    override fun checkPresentQuery(queryString: String) =
        movieAppDatabase.successfulQueryDao.checkPresentQuery(queryString)

    override fun addSuccessfulQuery(query: SuccessfulQuery) = movieAppDatabase.successfulQueryDao.addQuery(query)

    override fun searchMovie(movieName: String, pageNumber: Int)= movieSearch.search(movieName,pageNumber)
}