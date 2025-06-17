package com.example.gardenhelper.interactors_tests

import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.impl.NotificationsInteractorImpl
import com.example.gardenhelper.domain.models.notes.Notification
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class NotificationsIntaractorImplTest {
    private lateinit var interactor: NotificationsInteractorImpl
    private lateinit var mockRepository: NotificationsRepository
    private lateinit var testNotification: Notification

    @Before
    fun setUp() {
        mockRepository = mock(NotificationsRepository::class.java)
        interactor = NotificationsInteractorImpl(mockRepository)

        testNotification = Notification(
            id = 1,
            title = "Test Title",
            message = "Test Message",
            time = "12:00",
            isActive = true
        )
    }

    @Test
    fun `addNotification should call repository`() = runTest {
        // Act
        interactor.addNotification(testNotification)

        // Assert
        verify(mockRepository).addNotification(testNotification)
    }

    @Test
    fun `deleteNotification should call repository with correct id`() = runTest {
        // Arrange
        val testId = 123

        // Act
        interactor.deleteNotification(testId)

        // Assert
        verify(mockRepository).deleteNotification(testId)
    }

    @Test
    fun `getNotificationById should return notification from repository`() = runTest {
        // Arrange
        `when`(mockRepository.getNotificationById(testNotification.id))
            .thenReturn(testNotification)

        // Act
        val result = interactor.getNotificationById(testNotification.id)

        // Assert
        assertEquals(testNotification, result)
        verify(mockRepository).getNotificationById(testNotification.id)
    }

    @Test
    fun `getNotifications should return filtered notifications`() = runTest {
        // Arrange
        val testIds = listOf(1, 3)
        val expectedNotifications = listOf(
            Notification(id = 1, title = "Note 1"),
            Notification(id = 3, title = "Note 3")
        )
        `when`(mockRepository.getNotifications(testIds)).thenReturn(expectedNotifications)

        // Act
        val result = interactor.getNotifications(testIds)

        // Assert
        assertEquals(expectedNotifications, result)
        verify(mockRepository).getNotifications(testIds)
    }

    @Test
    fun `turnNotification should call repository with correct parameters`() = runTest {
        // Arrange
        val testId = 456
        val testIsOn = true

        // Act
        interactor.turnNotification(testIsOn, testId)

        // Assert
        verify(mockRepository).turnNotification(testIsOn, testId)
    }
}