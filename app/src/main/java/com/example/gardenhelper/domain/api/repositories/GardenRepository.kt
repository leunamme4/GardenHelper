package com.example.gardenhelper.domain.api.repositories

import com.example.gardenhelper.data.db.entity.GardenEntity
import com.example.gardenhelper.domain.models.garden.Garden

interface GardenRepository {
    suspend fun addGarden(garden: Garden)

    suspend fun deleteGarden(id: Int)

    suspend fun getGardens(): List<Garden>

    suspend fun getGardenById(id: Int): Garden
}