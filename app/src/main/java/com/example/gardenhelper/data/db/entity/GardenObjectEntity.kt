package com.example.gardenhelper.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_objects")
data class GardenObjectEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var type: String,
    var description: String,
    var icon: String,
    var notes: String, //json массива id
    var notifications: String //json массива id
) {
    constructor() : this(0, "", "", "", "", "", "")
}
