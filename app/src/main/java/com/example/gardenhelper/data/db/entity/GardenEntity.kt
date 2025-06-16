package com.example.gardenhelper.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gardens")
data class GardenEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var objects: String,
    var icon: String,
    var width: Int,
    var height: Int
) {
    constructor() : this(0, "", "", "", 0, 0)
}
