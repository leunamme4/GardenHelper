package com.example.gardenhelper.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gardenhelper.db.dao.CalendarDao
import com.example.gardenhelper.db.dao.GardenDao
import com.example.gardenhelper.db.dao.GardenObjectDao
import com.example.gardenhelper.db.dao.NotesDao
import com.example.gardenhelper.db.dao.NotificationsDao
import com.example.gardenhelper.db.entity.CalendarDayEntity
import com.example.gardenhelper.db.entity.GardenEntity
import com.example.gardenhelper.db.entity.GardenObjectEntity
import com.example.gardenhelper.db.entity.NoteEntity
import com.example.gardenhelper.db.entity.NotificationEntity

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