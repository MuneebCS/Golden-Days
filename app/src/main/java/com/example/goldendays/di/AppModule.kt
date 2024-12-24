package com.example.goldendays.di

import android.app.Application
import androidx.room.Room
import com.example.goldendays.data.database.GoldenDaysDB
import com.example.goldendays.data.database.EventDao
import com.example.goldendays.data.database.MediaDao
import com.example.goldendays.data.repository.EventRepo
import com.example.goldendays.data.repository.MediaRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): GoldenDaysDB {
        return Room.databaseBuilder(
            app,
            GoldenDaysDB::class.java,
            GoldenDaysDB.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventDao(db: GoldenDaysDB): EventDao {
        return db.eventDao()
    }

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepo {
        return EventRepo(eventDao)
    }

    @Provides
    @Singleton
    fun provideMediaDao(db: GoldenDaysDB): MediaDao {
        return db.mediaDao()
    }

    @Provides
    @Singleton
    fun provideMediaRepository(mediaDao: MediaDao): MediaRepo {
        return MediaRepo(mediaDao)
    }
}
