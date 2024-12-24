package com.example.goldendays.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goldendays.data.entities.Event
import com.example.goldendays.data.entities.Media

@Database(entities = [Event::class, Media::class], version = 1, exportSchema = false)
abstract class GoldenDaysDB : RoomDatabase(){
    abstract fun eventDao(): EventDao
    abstract fun mediaDao(): MediaDao

    companion object {
        const val DATABASE_NAME = "golden_days_db"
    }

}