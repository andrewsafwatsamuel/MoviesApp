package com.moviesapp.domain.databaseGateWay

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moviesapp.SuccessfulQuery
import com.moviesapp.domain.engine.applicationLiveData
import com.moviesapp.domain.engine.getApplication

const val DATABASE_NAME = "DatabaseGateway.db"

val movieAppDatabase by lazy {
    Room.databaseBuilder(
        applicationLiveData.getApplication(),
        MovieAppDatabase::class.java,
        DATABASE_NAME
    ).build()
}

@Database(entities = [SuccessfulQuery::class], version = 1, exportSchema = false)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract val successfulQueryDao: SuccessfulQueryDao
}