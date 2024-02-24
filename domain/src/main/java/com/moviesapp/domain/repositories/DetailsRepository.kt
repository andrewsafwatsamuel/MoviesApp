package com.moviesapp.domain.repositories

import com.moviesapp.CreditsResponse
import com.moviesapp.DetailsResponse
import com.moviesapp.MovieResponse
import com.moviesapp.domain.serverGateWay.movieDetails
import io.reactivex.Single

interface DetailsRepository {
    fun retrieveDetails(id: Long): Single<DetailsResponse>
    fun retrieveCredits(id: Long): Single<CreditsResponse>
    fun retrieveRelated(id: Long, page: Int): Single<MovieResponse>
}

val detailsRepository by lazy { DetailsRepositoryImplementer() }

class DetailsRepositoryImplementer : DetailsRepository {

    override fun retrieveDetails(id: Long) = movieDetails.retrieveDetails(id)

    override fun retrieveCredits(id: Long) = movieDetails.retrieveCredits(id)

    override fun retrieveRelated(id: Long, page: Int): Single<MovieResponse> =
        movieDetails.retrieveRelated(id, page)
}