package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.models.notes.Notification

class NotificationsRepositoryImpl(
    val database: AppDatabase,
    private val converter: EntityConverter
) : NotificationsRepository {
    override suspend fun addNotification(notification: Notification) {
        database.notificationsDao().addNotification(converter.notificationToEntity(notification))
    }

    override suspend fun deleteNotification(id: Int) {
        database.notificationsDao().deleteNotification(id)
    }

    override suspend fun getNotificationById(id: Int): Notification {
        return converter.notificationToDomain(database.notificationsDao().getNotificationById(id))
    }

    override suspend fun getNotifications(ids: List<Int>): List<Notification> {
        val notifications = database.notificationsDao().getNotifications()
            ?.map { converter.notificationToDomain(it) } ?: emptyList()

        val result: MutableList<Notification> = mutableListOf()

        ids.forEach { id ->
            notifications.forEach { notification ->
                if (notification.id == id) result.add(notification)
            }
        }

        return result
    }

    override suspend fun turnNotification(isOn: Boolean, notificationId: Int) {
        val notification = getNotificationById(notificationId)
        notification.isActive = isOn
        addNotification(notification)
    }
}