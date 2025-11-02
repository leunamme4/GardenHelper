package com.example.gardenhelper.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    var id: Int,
    var title: String,
    var message: String,
    var time: String,
    var isActive: Boolean
) {
    constructor() : this(0, "", "", "", false)
}