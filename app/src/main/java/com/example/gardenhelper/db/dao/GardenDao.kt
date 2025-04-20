package com.example.gardenhelper.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.db.entity.GardenEntity

@Dao
interface GardenDao {

    @Insert(entity = GardenEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGarden(garden: GardenEntity)

    @Query("DELETE FROM gardens WHERE id == :id")
    suspend fun deleteGarden(id: Int)

    @Query("SELECT * FROM gardens")
    suspend fun getGardens(): List<GardenEntity>
}