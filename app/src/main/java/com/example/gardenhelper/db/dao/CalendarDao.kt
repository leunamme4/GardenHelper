package com.example.gardenhelper.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.db.entity.CalendarDayEntity

@Dao
interface CalendarDao {

    @Insert(entity = CalendarDayEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCalendarDay(day: CalendarDayEntity)

    @Query("SELECT * FROM calendar_days WHERE date == :date")
    suspend fun getCalendarDay(date: String): CalendarDayEntity
}