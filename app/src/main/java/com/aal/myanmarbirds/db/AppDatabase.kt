package com.aal.myanmarbirds.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aal.myanmarbirds.db.daos.UserDao
import com.aal.myanmarbirds.db.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
