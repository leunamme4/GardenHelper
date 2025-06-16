package com.example.gardenhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.data.db.entity.CalendarDayEntity

@Dao
interface CalendarDao {

    @Insert(entity = CalendarDayEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCalendarDay(day: CalendarDayEntity)

    @Query("SELECT * FROM calendar_days WHERE date == :date")
    suspend fun getCalendarDay(date: String): CalendarDayEntity?

    @Query("SELECT * FROM calendar_days")
    suspend fun getCalendarDaysEntities(): List<CalendarDayEntity>?

    @Query("DELETE FROM calendar_days")
    suspend fun clearCalendarDays()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarDays(days: List<CalendarDayEntity>)
}