package com.example.domain.repositories

import com.example.CreditsResponse
import com.example.DetailsResponse
import com.example.MovieResponse
import com.example.domain.serverGateWay.apiEndPoints
import io.reactivex.Single

interface DetailsRepository {
    suspend fun retrieveDetails(id: Long): DetailsResponse
    suspend fun retrieveCredits(id: Long): CreditsResponse
    suspend fun retrieveRelated(id: Long, page: Int): MovieResponse
}

val detailsRepository by lazy { DetailsRepositoryImplementer() }

class DetailsRepositoryImplementer : DetailsRepository {

    override suspend fun retrieveDetails(id: Long) = apiEndPoints.retrieveDetails(id)

    override suspend fun retrieveCredits(id: Long) = apiEndPoints.retrieveCredits(id)

    override suspend fun retrieveRelated(id: Long, page: Int) =
        apiEndPoints.retrieveRelated(id, page)
}