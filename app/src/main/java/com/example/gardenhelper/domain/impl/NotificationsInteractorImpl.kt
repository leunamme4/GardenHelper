package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.models.notes.Notification

class NotificationsInteractorImpl(private val repository: NotificationsRepository) :
    NotificationsInteractor {
    override suspend fun addNotification(notification: Notification) {
        repository.addNotification(notification)
    }

    override suspend fun deleteNotification(id: Int) {
        repository.deleteNotification(id)
    }

    override suspend fun getNotificationById(id: Int): Notification {
        return repository.getNotificationById(id)
    }

    override suspend fun getNotifications(ids: List<Int>): List<Notification> {
        return repository.getNotifications(ids)
    }

    override suspend fun turnNotification(isOn: Boolean, notificationId: Int) {
        repository.turnNotification(isOn, notificationId)
    }
}