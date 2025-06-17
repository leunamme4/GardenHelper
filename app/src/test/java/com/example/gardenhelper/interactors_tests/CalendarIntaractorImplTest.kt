package com.example.gardenhelper.interactors_tests

import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.impl.CalendarInteractorImpl
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.weather.Condition
import com.example.gardenhelper.domain.models.weather.CurrentWeather
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse
import com.example.gardenhelper.domain.models.weather.Location
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any


class CalendarIntaractorImplTest {
    private lateinit var interactor: CalendarInteractorImpl
    private lateinit var mockRepository: CalendarRepository
    private lateinit var testWeatherResponse: CurrentWeatherResponse
    private lateinit var testCalendarDay: CalendarDay

    @Before
    fun setUp() {
        mockRepository = mock(CalendarRepository::class.java)
        interactor = CalendarInteractorImpl(mockRepository)

        testWeatherResponse = createTestWeatherResponse()
        testCalendarDay = createTestCalendarDay()
    }


    @Test
    fun `containsDay should return result from repository`() = runTest {
        // Arrange
        val testDate = "2023-12-31"
        `when`(mockRepository.containsDay(testDate)).thenReturn(true)

        // Act
        val result = interactor.containsDay(testDate)

        // Assert
        assertTrue(result)
        verify(mockRepository).containsDay(testDate)
    }

    @Test
    fun `getCalendarDay should return day from repository`() = runTest {
        // Arrange
        val testDate = "2023-12-31"
        `when`(mockRepository.getCalendarDay(testDate)).thenReturn(testCalendarDay)

        // Act
        val result = interactor.getCalendarDay(testDate)

        // Assert
        assertEquals(testCalendarDay, result)
        verify(mockRepository).getCalendarDay(testDate)
    }

    @Test
    fun `addWeatherData should call repository`() = runTest {
        // Arrange
        `when`(mockRepository.addWeatherData(any())).then { }

        // Act
        interactor.addWeatherData(testCalendarDay)

        // Assert
        verify(mockRepository).addWeatherData(testCalendarDay)
    }

    private fun createTestWeatherResponse(): CurrentWeatherResponse {
        return CurrentWeatherResponse(
            location = Location(
                name = "Moscow",
                region = "Moscow",
                country = "Russia",
                lat = 55.7558,
                lon = 37.6176,
                tz_id = "Europe/Moscow",
                localtime_epoch = 1700000000,
                localtime = "2023-12-31 12:00"
            ),
            current = CurrentWeather(
                cloud = 75,
                condition = Condition(
                    code = 1003,
                    icon = "//cdn.weatherapi.com/weather/64x64/day/116.png",
                    text = "Partly cloudy"
                ),
                feelslike_c = -5.0,
                feelslike_f = 23.0,
                gust_kph = 15.0,
                gust_mph = 9.3,
                humidity = 85,
                is_day = 1,
                last_updated = "2023-12-31 12:00",
                last_updated_epoch = 1700000000,
                precip_in = 0.0,
                precip_mm = 0.0,
                pressure_in = 29.92,
                pressure_mb = 1013.0,
                temp_c = -2.0,
                temp_f = 28.4,
                uv = 1.0,
                vis_km = 10.0,
                vis_miles = 6.0,
                wind_degree = 180,
                wind_dir = "S",
                wind_kph = 10.0,
                wind_mph = 6.2
            )
        )
    }

    private fun createTestCalendarDay(): CalendarDay {
        return CalendarDay(
            date = "2023-12-31",
            weather = createTestWeatherResponse(),
            notes = mutableListOf(1, 2, 3))
    }
}