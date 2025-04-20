package com.example.gardenhelper.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: Int,
    var title: String,
    var message: String,
    val time: String,
    var isActive: Boolean
)