package com.example.domain.databaseGateWay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.SuccessfulQuery

@Dao
interface SuccessfulQueryDao {
    @Query("SELECT * FROM SuccessfulQuery")
    fun queryAll(): List<SuccessfulQuery>

    @Query("SELECT * FROM SuccessfulQuery WHERE queryString = :queryString ")
    fun checkPresentQuery(queryString: String): SuccessfulQuery

    @Insert
    fun addQuery(query: SuccessfulQuery)
}