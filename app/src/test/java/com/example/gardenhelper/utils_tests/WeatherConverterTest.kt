package com.example.gardenhelper.utils_tests

import com.example.gardenhelper.data.dto.ConditionDto
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto
import com.example.gardenhelper.data.dto.LocationDto
import com.example.gardenhelper.data.dto.WeatherConverter
import com.example.gardenhelper.domain.models.weather.Condition
import com.example.gardenhelper.domain.models.weather.CurrentWeather
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse
import com.example.gardenhelper.domain.models.weather.Location
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeatherConverterTest {

    private val converter = WeatherConverter()

    @Test
    fun `mapCurrentWeatherResponse should convert DTO to Response correctly`() {
        // Given
        val dto = createTestCurrentWeatherResponseDto()

        // When
        val result = converter.mapCurrentWeatherResponse(dto)

        // Then
        assertEquals(dto.location.name, result.location.name)
        assertEquals(dto.current.temp_c, result.current.temp_c)
        assertEquals(dto.current.condition.text, result.current.condition.text)
    }

    @Test
    fun `mapCurrentWeatherResponseBack should convert Response to DTO correctly`() {
        // Given
        val response = createTestCurrentWeatherResponse()

        // When
        val result = converter.mapCurrentWeatherResponseBack(response)

        // Then
        assertEquals(response.location.name, result.location.name)
        assertEquals(response.current.temp_c, result.current.temp_c)
        assertEquals(response.current.condition.text, result.current.condition.text)
    }

    @Test
    fun `mapCurrentWeather should convert CurrentWeatherDto to CurrentWeather correctly`() {
        // Given
        val dto = createTestCurrentWeatherDto()

        // When
        val result = converter.mapCurrentWeather(dto)

        // Then
        assertEquals(dto.temp_c, result.temp_c)
        assertEquals(dto.humidity, result.humidity)
        assertEquals(dto.condition.text, result.condition.text)
    }

    @Test
    fun `mapCurrentWeatherBack should convert CurrentWeather to CurrentWeatherDto correctly`() {
        // Given
        val weather = createTestCurrentWeather()

        // When
        val result = converter.mapCurrentWeatherBack(weather)

        // Then
        assertEquals(weather.temp_c, result.temp_c)
        assertEquals(weather.humidity, result.humidity)
        assertEquals(weather.condition.text, result.condition.text)
    }

    @Test
    fun `mapCondition should convert ConditionDto to Condition correctly`() {
        // Given
        val dto = ConditionDto(code = 1000, icon = "sunny.png", text = "Sunny")

        // When
        val result = converter.mapCondition(dto)

        // Then
        assertEquals(dto.code, result.code)
        assertEquals(dto.icon, result.icon)
        assertEquals(dto.text, result.text)
    }

    @Test
    fun `mapConditionBack should convert Condition to ConditionDto correctly`() {
        // Given
        val condition = Condition(code = 1000, icon = "sunny.png", text = "Sunny")

        // When
        val result = converter.mapConditionBack(condition)

        // Then
        assertEquals(condition.code, result.code)
        assertEquals(condition.icon, result.icon)
        assertEquals(condition.text, result.text)
    }

    @Test
    fun `mapLocation should convert LocationDto to Location correctly`() {
        // Given
        val dto = createTestLocationDto()

        // When
        val result = converter.mapLocation(dto)

        // Then
        assertEquals(dto.name, result.name)
        assertEquals(dto.country, result.country)
        assertEquals(dto.lat, result.lat)
    }

    @Test
    fun `mapLocationBack should convert Location to LocationDto correctly`() {
        // Given
        val location = createTestLocation()

        // When
        val result = converter.mapLocationBack(location)

        // Then
        assertEquals(location.name, result.name)
        assertEquals(location.country, result.country)
        assertEquals(location.lat, result.lat)
    }

    // Helper methods to create test data
    private fun createTestCurrentWeatherResponseDto(): CurrentWeatherResponseDto {
        return CurrentWeatherResponseDto(
            location = createTestLocationDto(),
            current = createTestCurrentWeatherDto()
        )
    }

    private fun createTestCurrentWeatherResponse(): CurrentWeatherResponse {
        return CurrentWeatherResponse(
            location = createTestLocation(),
            current = createTestCurrentWeather()
        )
    }

    private fun createTestLocationDto(): LocationDto {
        return LocationDto(
            name = "London",
            region = "City of London",
            country = "UK",
            lat = 51.51,
            lon = -0.13,
            tz_id = "Europe/London",
            localtime_epoch = 1672531200,
            localtime = "2023-01-01 12:00"
        )
    }

    private fun createTestLocation(): Location {
        return Location(
            name = "London",
            region = "City of London",
            country = "UK",
            lat = 51.51,
            lon = -0.13,
            tz_id = "Europe/London",
            localtime_epoch = 1672531200,
            localtime = "2023-01-01 12:00"
        )
    }

    private fun createTestCurrentWeatherDto(): CurrentWeatherDto {
        return CurrentWeatherDto(
            cloud = 25,
            condition = ConditionDto(code = 1000, icon = "sunny.png", text = "Sunny"),
            feelslike_c = 22.0,
            feelslike_f = 71.6,
            gust_kph = 15.0,
            gust_mph = 9.3,
            humidity = 65,
            is_day = 1,
            last_updated = "2023-01-01 12:00",
            last_updated_epoch = 1672531200,
            precip_in = 0.0,
            precip_mm = 0.0,
            pressure_in = 30.0,
            pressure_mb = 1015.0,
            temp_c = 23.0,
            temp_f = 73.4,
            uv = 5.0,
            vis_km = 10.0,
            vis_miles = 6.0,
            wind_degree = 180,
            wind_dir = "S",
            wind_kph = 10.0,
            wind_mph = 6.2
        )
    }

    private fun createTestCurrentWeather(): CurrentWeather {
        return CurrentWeather(
            cloud = 25,
            condition = Condition(code = 1000, icon = "sunny.png", text = "Sunny"),
            feelslike_c = 22.0,
            feelslike_f = 71.6,
            gust_kph = 15.0,
            gust_mph = 9.3,
            humidity = 65,
            is_day = 1,
            last_updated = "2023-01-01 12:00",
            last_updated_epoch = 1672531200,
            precip_in = 0.0,
            precip_mm = 0.0,
            pressure_in = 30.0,
            pressure_mb = 1015.0,
            temp_c = 23.0,
            temp_f = 73.4,
            uv = 5.0,
            vis_km = 10.0,
            vis_miles = 6.0,
            wind_degree = 180,
            wind_dir = "S",
            wind_kph = 10.0,
            wind_mph = 6.2
        )
    }
}