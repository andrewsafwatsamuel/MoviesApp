package com.example.domain.databaseGateWay

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.SuccessfulQuery
import com.example.domain.engine.applicationLiveData
import com.example.domain.engine.getApplication

val movieAppDatabase by lazy {
    Room.databaseBuilder(
        applicationLiveData.getApplication(),
        MovieAppDatabase::class.java,
        DATABASE_NAME
    )
        .build()
}

@Database(entities = [SuccessfulQuery::class], version = 1, exportSchema = false)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract val successfulQueryDao: SuccessfulQueryDao
}