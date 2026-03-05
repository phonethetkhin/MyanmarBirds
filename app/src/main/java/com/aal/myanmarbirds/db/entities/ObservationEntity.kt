package com.aal.myanmarbirds.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "observations")
data class ObservationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val birdName: String,
    val note: String,
    val date: Long,
    val latitude: Double?,
    val longitude: Double?,
    val imagePath: String?,
    val bodyColor: String

)