package com.example.gardenhelper.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_days")
data class CalendarDayEntity(
    @PrimaryKey
    var date: String,
    var weather: String,
    var notes: String
) {
    constructor() : this("", "", "")
}
