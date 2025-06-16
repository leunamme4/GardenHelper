package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import com.example.gardenhelper.domain.models.garden.GardenObject

class ObjectsInteractorImpl(private val repository: ObjectsRepository): ObjectsInteractor {
    override suspend fun addObject(obj: GardenObject) {
        repository.addObject(obj)
    }

    override suspend fun deleteObject(id: Int) {
        repository.deleteObject(id)
    }

    override suspend fun getObjectById(id: Int): GardenObject {
        return repository.getObjectById(id)
    }

    override suspend fun getObjects(): List<GardenObject> {
        return repository.getObjects()
    }

    override suspend fun uploadAllToFirestore() {
        repository.uploadAllToFirestore()
    }

    override suspend fun downloadAllFromFirestore() {
        repository.downloadAllFromFirestore()
    }
}