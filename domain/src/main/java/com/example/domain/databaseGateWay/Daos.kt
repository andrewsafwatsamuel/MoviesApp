package com.example.domain.databaseGateWay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.SuccessfulQuery

//read me
//I put daos to single file as they are not large in number and related to each other

@Dao
interface GenreDao {
    @Query("SELECT name FROM Genre WHERE id IN (:genreIds)")
    fun retrieveGenres(genreIds: List<Int>): List<String>
}

@Dao
interface SuccessfulQueryDao {
    @Query("SELECT * FROM SuccessfulQuery")
    fun queryAll(): List<SuccessfulQuery>

    @Query("SELECT * FROM SuccessfulQuery WHERE queryString = :queryString ")
    fun checkPresentQuery(queryString: String): SuccessfulQuery

    @Insert
    fun addQuery(query: SuccessfulQuery)
}