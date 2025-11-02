package com.example.gardenhelper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gardenhelper.data.db.dao.CalendarDao
import com.example.gardenhelper.data.db.dao.GardenDao
import com.example.gardenhelper.data.db.dao.GardenObjectDao
import com.example.gardenhelper.data.db.dao.NotesDao
import com.example.gardenhelper.data.db.dao.NotificationsDao
import com.example.gardenhelper.data.db.entity.CalendarDayEntity
import com.example.gardenhelper.data.db.entity.GardenEntity
import com.example.gardenhelper.data.db.entity.GardenObjectEntity
import com.example.gardenhelper.data.db.entity.NoteEntity
import com.example.gardenhelper.data.db.entity.NotificationEntity

@Database(
    version = 1,
    entities = [CalendarDayEntity::class, GardenEntity::class, GardenObjectEntity::class, NoteEntity::class, NotificationEntity::class]
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun calendarDao(): CalendarDao

    abstract fun gardenDao(): GardenDao

    abstract fun gardenObjectDao(): GardenObjectDao

    abstract fun notesDao(): NotesDao

    abstract fun notificationsDao(): NotificationsDao
}