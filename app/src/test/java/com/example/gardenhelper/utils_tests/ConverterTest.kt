package com.example.gardenhelper.utils_tests

import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.entity.CalendarDayEntity
import com.example.gardenhelper.data.db.entity.GardenObjectEntity
import com.example.gardenhelper.data.db.entity.NoteEntity
import com.example.gardenhelper.data.db.entity.NotificationEntity
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.domain.models.weather.Condition
import com.example.gardenhelper.domain.models.weather.CurrentWeather
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse
import com.example.gardenhelper.domain.models.weather.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class ConverterTest {

    private lateinit var converter: EntityConverter
    private val gson = Gson()

    @Before
    fun setup() {
        converter = EntityConverter()
    }

    // CalendarDay tests
    @Test
    fun `toEntity converts CalendarDay to CalendarDayEntity with correct data`() {
        // Arrange
        val weather = CurrentWeatherResponse(
            location = Location("Moscow", "Moscow", "Russia", 55.75, 37.62,
                "Europe/Moscow", 1672531200, "2023-01-01 12:00"),
            current = createTestCurrentWeather()
        )
        val notes = mutableListOf(1, 2, 3)
        val calendarDay = CalendarDay("2023-01-01", weather, notes)

        // Act
        val result = converter.toEntity(calendarDay)

        // Assert
        assertEquals("2023-01-01", result.date)
        assertEquals(notes, gson.fromJson(result.notes, object : TypeToken<MutableList<Int>>() {}.type))
    }

    @Test
    fun `toDomain converts CalendarDayEntity to CalendarDay with correct data`() {
        // Arrange
        val weather = CurrentWeatherResponse(
            location = Location("London", "England", "UK", 51.51, -0.13,
                "Europe/London", 1672531200, "2023-01-01 10:00"),
            current = createTestCurrentWeather()
        )
        val notes = mutableListOf(4, 5, 6)
        val entity = CalendarDayEntity(
            date = "2023-01-01",
            weather = gson.toJson(weather),
            notes = gson.toJson(notes)
        )

        // Act
        val result = converter.toDomain(entity)

        // Assert
        assertEquals("2023-01-01", result.date)
        assertEquals(notes, result.notes)
    }

    @Test
    fun `toDomain handles null notes by returning empty list`() {
        // Arrange
        val weather = CurrentWeatherResponse(
            location = Location("Paris", "Ile-de-France", "France", 48.86, 2.35,
                "Europe/Paris", 1672531200, "2023-01-01 11:00"),
            current = createTestCurrentWeather()
        )
        val entity = CalendarDayEntity(
            date = "2023-01-01",
            weather = gson.toJson(weather),
            notes = ""
        )

        // Act
        val result = converter.toDomain(entity)

        // Assert
        assertTrue(result.notes.isEmpty())
    }

    // Note tests
    @Test
    fun `noteToEntity converts Note to NoteEntity with correct data`() {
        // Arrange
        val images = mutableListOf("image1.jpg", "image2.png")
        val note = Note(1, "Shopping", "Buy milk", images)

        // Act
        val result = converter.noteToEntity(note)

        // Assert
        assertEquals(1, result.id)
        assertEquals("Shopping", result.name)
        assertEquals("Buy milk", result.text)
        assertEquals(images, gson.fromJson(result.images, object : TypeToken<MutableList<String>>() {}.type))
    }

    @Test
    fun `noteToDomain converts NoteEntity to Note with correct data`() {
        // Arrange
        val images = mutableListOf("photo1.jpg", "photo2.png")
        val entity = NoteEntity(
            id = 2,
            name = "Ideas",
            text = "New project",
            images = gson.toJson(images)
        )

        // Act
        val result = converter.noteToDomain(entity)

        // Assert
        assertEquals(2, result.id)
        assertEquals("Ideas", result.name)
        assertEquals("New project", result.text)
        assertEquals(images, result.images)
    }

    // GardenObject tests
    @Test
    fun `gardenObjectToEntity converts GardenObject to GardenObjectEntity with correct data`() {
        // Arrange
        val notes = mutableListOf(101, 102)
        val notifications = mutableListOf(201, 202)
        val gardenObject = GardenObject(
            id = 1,
            name = "Rose",
            type = "Flower",
            description = "Red rose",
            icon = "rose_icon",
            notes = notes,
            notifications = notifications
        )

        // Act
        val result = converter.gardenObjectToEntity(gardenObject)

        // Assert
        assertEquals(1, result.id)
        assertEquals("Rose", result.name)
        assertEquals("Flower", result.type)
        assertEquals("Red rose", result.description)
        assertEquals("rose_icon", result.icon)
        assertEquals(notes, gson.fromJson(result.notes, object : TypeToken<MutableList<Int>>() {}.type))
        assertEquals(notifications, gson.fromJson(result.notifications, object : TypeToken<MutableList<Int>>() {}.type))
    }

    @Test
    fun `gardenObjectToDomain converts GardenObjectEntity to GardenObject with correct data`() {
        // Arrange
        val notes = mutableListOf(301, 302)
        val notifications = mutableListOf(401, 402)
        val entity = GardenObjectEntity(
            id = 2,
            name = "Apple Tree",
            type = "Tree",
            description = "Green apples",
            icon = "apple_icon",
            notes = gson.toJson(notes),
            notifications = gson.toJson(notifications)
        )

        // Act
        val result = converter.gardenObjectToDomain(entity)

        // Assert
        assertEquals(2, result.id)
        assertEquals("Apple Tree", result.name)
        assertEquals("Tree", result.type)
        assertEquals("Green apples", result.description)
        assertEquals("apple_icon", result.icon)
        assertEquals(notes, result.notes)
        assertEquals(notifications, result.notifications)
    }

    // Notification tests
    @Test
    fun `notificationToEntity converts Notification to NotificationEntity with correct data`() {
        // Arrange
        val notification = Notification(
            id = 1,
            title = "Reminder",
            message = "Water plants",
            time = "09:00",
            isActive = true
        )

        // Act
        val result = converter.notificationToEntity(notification)

        // Assert
        assertEquals(1, result.id)
        assertEquals("Reminder", result.title)
        assertEquals("Water plants", result.message)
        assertEquals("09:00", result.time)
        assertTrue(result.isActive)
    }

    @Test
    fun `notificationToDomain converts NotificationEntity to Notification with correct data`() {
        // Arrange
        val entity = NotificationEntity(
            id = 2,
            title = "Alert",
            message = "Fertilize soil",
            time = "18:00",
            isActive = false
        )

        // Act
        val result = converter.notificationToDomain(entity)

        // Assert
        assertEquals(2, result.id)
        assertEquals("Alert", result.title)
        assertEquals("Fertilize soil", result.message)
        assertEquals("18:00", result.time)
        assertFalse(result.isActive)
    }

    private fun createTestCurrentWeather(): CurrentWeather {
        return CurrentWeather(
            cloud = 75,
            condition = Condition(1000, "sunny", "Sunny"),
            feelslike_c = 25.0,
            feelslike_f = 77.0,
            gust_kph = 15.0,
            gust_mph = 9.3,
            humidity = 60,
            is_day = 1,
            last_updated = "2023-01-01 12:00",
            last_updated_epoch = 1672531200,
            precip_in = 0.0,
            precip_mm = 0.0,
            pressure_in = 30.0,
            pressure_mb = 1015.0,
            temp_c = 27.0,
            temp_f = 80.6,
            uv = 6.0,
            vis_km = 10.0,
            vis_miles = 6.2,
            wind_degree = 180,
            wind_dir = "S",
            wind_kph = 12.0,
            wind_mph = 7.5
        )
    }
}