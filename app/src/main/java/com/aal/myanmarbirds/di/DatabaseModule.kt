package com.aal.myanmarbirds.di

import android.content.Context
import androidx.room.Room
import com.aal.myanmarbirds.db.AppDatabase
import com.aal.myanmarbirds.db.daos.ObservationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "birds_database"
        ).build()
    }

    @Provides
    fun provideObservationDao(
        db: AppDatabase
    ): ObservationDao = db.observationDao()
}
