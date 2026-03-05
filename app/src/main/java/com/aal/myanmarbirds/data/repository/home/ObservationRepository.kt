package com.aal.myanmarbirds.data.repository.home

import com.aal.myanmarbirds.db.daos.ObservationDao
import com.aal.myanmarbirds.db.entities.ObservationEntity
import javax.inject.Inject

class ObservationRepository @Inject constructor(
    private val dao: ObservationDao
) {

    suspend fun insertObservation(observation: ObservationEntity) {
        dao.insertObservation(observation)
    }

    fun getAllObservations() = dao.getObservations()
}