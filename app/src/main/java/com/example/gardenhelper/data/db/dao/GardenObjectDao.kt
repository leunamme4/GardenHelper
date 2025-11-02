package com.example.gardenhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.data.db.entity.GardenObjectEntity

@Dao
interface GardenObjectDao {

    @Insert(entity = GardenObjectEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addObject(obj: GardenObjectEntity)

    @Query("DELETE FROM garden_objects WHERE id == :id")
    suspend fun deleteObject(id: Int)

    @Query("SELECT * FROM garden_objects WHERE id == :id")
    suspend fun getObjectById(id: Int): GardenObjectEntity

    @Query("SELECT * FROM garden_objects")
    suspend fun getObjects(): List<GardenObjectEntity>?

    @Query("SELECT * FROM garden_objects WHERE type == :type")
    suspend fun getObjectsByType(type: String): List<GardenObjectEntity>

    @Query("SELECT * FROM garden_objects WHERE name == :name")
    suspend fun getObjectsByName(name: String): List<GardenObjectEntity>

    @Query("DELETE FROM garden_objects")
    suspend fun clearGardenObjects()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(objects: List<GardenObjectEntity>)
}