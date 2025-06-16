package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.domain.api.repositories.GardenRepository
import com.example.gardenhelper.domain.models.garden.Garden

class GardenRepositoryImpl(
    private val database: AppDatabase,
    private val converter: EntityConverter
) : GardenRepository {
    override suspend fun addGarden(garden: Garden) {
        database.gardenDao().addGarden(converter.gardenToEntity(garden))
    }

    override suspend fun deleteGarden(id: Int) {
        database.gardenDao().deleteGarden(id)
    }

    override suspend fun getGardens(): List<Garden> {
        val result = database.gardenDao().getGardens() ?: emptyList()
        return if (result.isNotEmpty()) {
            result.map { converter.gardenToDomain(it) }
        } else {
            emptyList()
        }
    }

    override suspend fun getGardenById(id: Int): Garden {
        return converter.gardenToDomain(database.gardenDao().getGardenById(id))
    }

}