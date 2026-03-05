package com.aal.myanmarbirds.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aal.myanmarbirds.db.entities.ObservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(observation: ObservationEntity)

    @Query("SELECT * FROM observations ORDER BY date DESC")
    fun getObservations(): Flow<List<ObservationEntity>>

    @Delete
    suspend fun deleteObservation(observation: ObservationEntity)
}