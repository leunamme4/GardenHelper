package com.example.gardenhelper.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_days")
data class CalendarDayEntity(
    @PrimaryKey
    val date: String,
    val weather: String,
    val notes: String
)
