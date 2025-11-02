package com.example.gardenhelper.domain.api.interactors

import com.example.gardenhelper.domain.models.notes.Notification

interface NotificationsInteractor {
    suspend fun addNotification(notification: Notification)

    suspend fun deleteNotification(id: Int)

    suspend fun getNotificationById(id: Int): Notification

    suspend fun getNotifications(ids: List<Int>): List<Notification>

    suspend fun turnNotification(isOn: Boolean, notificationId: Int)
}