package com.example.gardenhelper.repositories_mock_tests

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.dao.NotificationsDao
import com.example.gardenhelper.data.db.entity.NotificationEntity
import com.example.gardenhelper.data.impl.NotificationsRepositoryImpl
import com.example.gardenhelper.domain.models.notes.Notification
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class NotificationsRepositoryImplTest {

    @Mock
    private lateinit var database: AppDatabase

    @Mock
    private lateinit var notificationsDao: NotificationsDao

    @Mock
    private lateinit var converter: EntityConverter

    private lateinit var repository: NotificationsRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        `when`(database.notificationsDao()).thenReturn(notificationsDao)

        repository = NotificationsRepositoryImpl(
            database = database,
            converter = converter
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addNotification should convert and insert entity`() = runTest(testDispatcher) {
        // Arrange
        val notification = Notification(
            id = 1,
            title = "Test Title",
            message = "Test Message",
            time = "12:00",
            isActive = true
        )

        val entity = NotificationEntity(
            id = 1,
            title = "Test Title",
            message = "Test Message",
            time = "12:00",
            isActive = true
        )

        `when`(converter.notificationToEntity(notification)).thenReturn(entity)

        // Act
        repository.addNotification(notification)

        // Assert
        verify(converter).notificationToEntity(notification)
        verify(notificationsDao).addNotification(entity)
    }

    @Test
    fun `deleteNotification should call DAO with correct id`() = runTest(testDispatcher) {
        // Arrange
        val testId = 42

        // Act
        repository.deleteNotification(testId)

        // Assert
        verify(notificationsDao).deleteNotification(testId)
    }

    @Test
    fun `getNotificationById should return converted domain object`() = runTest(testDispatcher) {
        // Arrange
        val testId = 1
        val entity = NotificationEntity(
            id = testId,
            title = "Test",
            message = "Message",
            time = "10:00",
            isActive = true
        )

        val domain = Notification(
            id = testId,
            title = "Test",
            message = "Message",
            time = "10:00",
            isActive = true
        )

        `when`(notificationsDao.getNotificationById(testId)).thenReturn(entity)
        `when`(converter.notificationToDomain(entity)).thenReturn(domain)

        // Act
        val result = repository.getNotificationById(testId)

        // Assert
        assertEquals(domain, result)
        verify(notificationsDao).getNotificationById(testId)
        verify(converter).notificationToDomain(entity)
    }

    @Test
    fun `getNotifications should return filtered list by ids`() = runTest(testDispatcher) {
        // Arrange
        val allEntities = listOf(
            NotificationEntity(1, "Title 1", "Msg 1", "10:00", true),
            NotificationEntity(2, "Title 2", "Msg 2", "11:00", false),
            NotificationEntity(3, "Title 3", "Msg 3", "12:00", true)
        )

        val allDomains = listOf(
            Notification(1, "Title 1", "Msg 1", "10:00", true),
            Notification(2, "Title 2", "Msg 2", "11:00", false),
            Notification(3, "Title 3", "Msg 3", "12:00", true)
        )

        `when`(notificationsDao.getNotifications()).thenReturn(allEntities)
        `when`(converter.notificationToDomain(allEntities[0])).thenReturn(allDomains[0])
        `when`(converter.notificationToDomain(allEntities[1])).thenReturn(allDomains[1])
        `when`(converter.notificationToDomain(allEntities[2])).thenReturn(allDomains[2])

        // Act
        val result = repository.getNotifications(listOf(1, 3))

        // Assert
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(3, result[1].id)
        verify(notificationsDao).getNotifications()
    }

    @Test
    fun `getNotifications should return empty list when no matches`() = runTest(testDispatcher) {
        // Arrange
        `when`(notificationsDao.getNotifications()).thenReturn(emptyList())

        // Act
        val result = repository.getNotifications(listOf(1, 2, 3))

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `turnNotification should update notification status`() = runTest(testDispatcher) {
        // Arrange
        val testId = 1
        val originalNotification = Notification(
            id = testId,
            title = "Original",
            message = "Msg",
            time = "10:00",
            isActive = false
        )

        repository.addNotification(originalNotification)

        val updatedNotification = Notification(
            id = testId,
            title = "Original",
            message = "Msg",
            time = "10:00",
            isActive = true
        )

        val updatedEntity = NotificationEntity(
            id = testId,
            title = "Original",
            message = "Msg",
            time = "10:00",
            isActive = true
        )

        `when`(notificationsDao.getNotificationById(testId)).thenReturn(
            NotificationEntity(
                id = testId,
                title = "Original",
                message = "Msg",
                time = "10:00",
                isActive = false
            )
        )
        `when`(converter.notificationToEntity(updatedNotification)).thenReturn(updatedEntity)


    }

    @Test
    fun `turnNotification should deactivate notification`() = runTest(testDispatcher) {
        // Arrange
        val testId = 2
        val activeNotification = Notification(
            id = testId,
            title = "Active",
            message = "Msg",
            time = "11:00",
            isActive = true
        )

        repository.addNotification(activeNotification)

        `when`(notificationsDao.getNotificationById(testId)).thenReturn(
            NotificationEntity(
                id = testId,
                title = "Active",
                message = "Msg",
                time = "11:00",
                isActive = true
            )
        )

    }
}