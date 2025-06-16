package com.example.gardenhelper.domain.api.repositories

import com.example.gardenhelper.domain.models.garden.GardenObject

interface ObjectsRepository {
    suspend fun addObject(obj: GardenObject)

    suspend fun deleteObject(id: Int)

    suspend fun getObjectById(id: Int): GardenObject

    suspend fun getObjects(): List<GardenObject>

    suspend fun uploadAllToFirestore()

    suspend fun downloadAllFromFirestore()
}