package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.domain.api.interactors.GardenInteractor
import com.example.gardenhelper.domain.api.repositories.GardenRepository
import com.example.gardenhelper.domain.models.garden.Garden

class GardenInteractorImpl(private val repository: GardenRepository): GardenInteractor {
    override suspend fun addGarden(garden: Garden) {
        repository.addGarden(garden)
    }

    override suspend fun deleteGarden(id: Int) {
        repository.deleteGarden(id)
    }

    override suspend fun getGardens(): List<Garden> {
        return repository.getGardens()
    }

    override suspend fun getGardenById(id: Int): Garden {
        return repository.getGardenById(id)
    }
}