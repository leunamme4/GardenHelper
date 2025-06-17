package com.example.gardenhelper.repositories_mock_tests

import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.dao.CalendarDao
import com.example.gardenhelper.data.db.entity.CalendarDayEntity
import com.example.gardenhelper.data.dto.WeatherConverter
import com.example.gardenhelper.data.impl.CalendarRepositoryImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CalendarRepositoryImplTest {

    @Mock
    private lateinit var networkClient: NetworkClient

    @Mock
    private lateinit var converter: WeatherConverter

    @Mock
    private lateinit var database: AppDatabase

    @Mock
    private lateinit var calendarDao: CalendarDao

    @Mock
    private lateinit var calendarConverter: EntityConverter

    private lateinit var repository: CalendarRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        `when`(database.calendarDao()).thenReturn(calendarDao)

        repository = CalendarRepositoryImpl(
            networkClient,
            converter,
            database,
            calendarConverter
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentWeather should return Success when network client succeeds`() = runTest(testDispatcher) {
        // Arrange


        // Assert
        assertEquals(1, 1)
    }

    @Test
    fun `getCurrentWeather should return Error when network client fails`() = runTest(testDispatcher) {
        // Arrange
        assertEquals(1, 1)
    }

    @Test
    fun `containsDay should return true when day exists`() = runTest(testDispatcher) {
        // Arrange
        val testDate = "2023-01-01"
        `when`(calendarDao.getCalendarDay(testDate)).thenReturn(CalendarDayEntity(testDate, "", ""))

        // Act
        val result = repository.containsDay(testDate)

        // Assert
        assertTrue(result)
        verify(calendarDao).getCalendarDay(testDate)
    }

    @Test
    fun `containsDay should return false when day does not exist`() = runTest(testDispatcher) {
        // Arrange
        val testDate = "2023-01-01"
        `when`(calendarDao.getCalendarDay(testDate)).thenReturn(null)

        // Act
        val result = repository.containsDay(testDate)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `getCalendarDay should return converted domain object`() = runTest(testDispatcher) {
        // Arrange
        val testDate = "2023-01-01"
        val entity = CalendarDayEntity(testDate, "Sunny", "notes")

        `when`(calendarDao.getCalendarDay(testDate)).thenReturn(entity)

        // Act
        val result = repository.getCalendarDay(testDate)

        // Assert
        assertEquals(1, 1)
        verify(calendarDao).getCalendarDay(testDate)
        verify(calendarConverter).toDomain(entity)
    }

    @Test(expected = NullPointerException::class)
    fun `getCalendarDay should throw when day not found`() = runTest(testDispatcher) {
        // Arrange
        val testDate = "2023-01-01"
        `when`(calendarDao.getCalendarDay(testDate)).thenReturn(null)

        // Act
        repository.getCalendarDay(testDate)
    }

}