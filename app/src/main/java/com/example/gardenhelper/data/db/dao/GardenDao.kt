package com.example.gardenhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.data.db.entity.GardenEntity
import com.example.gardenhelper.data.db.entity.NoteEntity

@Dao
interface GardenDao {

    @Insert(entity = GardenEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGarden(garden: GardenEntity)

    @Query("DELETE FROM gardens WHERE id == :id")
    suspend fun deleteGarden(id: Int)

    @Query("SELECT * FROM gardens WHERE id == :id")
    suspend fun getGardenById(id: Int): GardenEntity

    @Query("SELECT * FROM gardens")
    suspend fun getGardens(): List<GardenEntity>?

    @Query("DELETE FROM gardens")
    suspend fun clearGardens()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGardens(gardens: List<GardenEntity>)
}