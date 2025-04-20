package com.example.gardenhelper.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.db.entity.NotificationEntity

@Dao
interface NotificationsDao {

    @Insert(entity = NotificationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications WHERE id == :id")
    suspend fun deleteNotification(id: Int)

    @Query("SELECT * FROM notifications WHERE id == :id")
    suspend fun getNotificationById(id: Int): NotificationEntity

    @Query("SELECT * FROM notifications")
    suspend fun getNotifications(): List<NotificationEntity>
}