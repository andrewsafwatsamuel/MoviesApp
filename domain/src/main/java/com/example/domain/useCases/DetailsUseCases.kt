package com.example.domain.useCases

import com.example.domain.repositories.DetailsRepository
import com.example.domain.repositories.detailsRepository

fun retrieveGenres(ids:List<Int>,repository: DetailsRepository=detailsRepository)=
        ids.let { if (it.isNotEmpty()) repository.retrieveGenres(it) else listOf() }
