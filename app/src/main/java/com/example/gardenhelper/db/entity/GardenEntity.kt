package com.example.gardenhelper.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gardens")
data class GardenEntity(
    @PrimaryKey
    val id: Int,
    var name: String,
    var objects: String
)
