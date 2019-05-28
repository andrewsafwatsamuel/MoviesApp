package com.example.domain.repositories

import com.example.domain.databaseGateWay.movieAppDatabase

interface DetailsRepository {
    fun retrieveGenres(genreIds: List<Int>): List<String>
}

val detailsRepository by lazy { DetailsRepositoryImplementer() }

class DetailsRepositoryImplementer : DetailsRepository {
    override fun retrieveGenres(genreIds: List<Int>): List<String> =
        movieAppDatabase.genreDao.retrieveGenres(genreIds)
}