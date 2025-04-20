package com.example.gardenhelper.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_objects")
data class GardenObjectEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var type: String,
    var description: String,
    val icon: String,
    var notes: String,
    var notifications: String
)
