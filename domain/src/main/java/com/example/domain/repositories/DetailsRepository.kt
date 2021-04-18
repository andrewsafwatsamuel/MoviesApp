package com.example.domain.repositories

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.domain.serverGateWay.apiEndPoints
import io.reactivex.Single

interface DetailsRepository {
    fun retrieveDetails(id: Long): Single<DetailsResponse>
    fun retrieveCredits(id: Long): Single<CreditsResponse>
    fun retrieveRelated(id: Long, page: Int): Single<MovieResponse>
}

val detailsRepository by lazy { DetailsRepositoryImplementer() }

class DetailsRepositoryImplementer : DetailsRepository {

    override fun retrieveDetails(id: Long) = apiEndPoints.retrieveDetails(id)

    override fun retrieveCredits(id: Long) = apiEndPoints.retrieveCredits(id)

    override fun retrieveRelated(id: Long, page: Int): Single<MovieResponse> =
        apiEndPoints.retrieveRelated(id, page)
}