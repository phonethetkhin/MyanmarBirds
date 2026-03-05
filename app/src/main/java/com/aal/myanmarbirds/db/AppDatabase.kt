package com.aal.myanmarbirds.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aal.myanmarbirds.db.daos.ObservationDao
import com.aal.myanmarbirds.db.entities.ObservationEntity

@Database(
    entities = [ObservationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun observationDao(): ObservationDao
}
