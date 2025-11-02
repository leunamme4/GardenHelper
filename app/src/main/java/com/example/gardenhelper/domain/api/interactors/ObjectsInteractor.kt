package com.example.gardenhelper.domain.api.interactors

import com.example.gardenhelper.domain.models.garden.GardenObject

interface ObjectsInteractor {
    suspend fun addObject(obj: GardenObject)

    suspend fun deleteObject(id: Int)

    suspend fun getObjectById(id: Int): GardenObject

    suspend fun getObjects(): List<GardenObject>

    suspend fun uploadAllToFirestore()

    suspend fun downloadAllFromFirestore()
}