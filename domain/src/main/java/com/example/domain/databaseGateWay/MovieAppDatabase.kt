package com.example.domain.databaseGateWay

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.Genre
import com.example.SuccessfulQuery
import com.example.domain.engine.applicationLiveData
import com.example.domain.engine.getApplication

val movieAppDatabase by lazy { initializeDatabase(applicationLiveData.getApplication()) }

@Database(entities = [SuccessfulQuery::class, Genre::class], version = 1, exportSchema = false)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract val successfulQueryDao: SuccessfulQueryDao
    abstract val genreDao: GenreDao
}