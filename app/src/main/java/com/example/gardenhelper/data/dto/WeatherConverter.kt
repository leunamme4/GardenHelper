package com.example.gardenhelper.data.dto

import com.example.gardenhelper.domain.models.weather.Condition
import com.example.gardenhelper.domain.models.weather.CurrentWeather
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse
import com.example.gardenhelper.domain.models.weather.Location

class WeatherConverter {

    fun mapCurrentWeatherResponse(dto: CurrentWeatherResponseDto): CurrentWeatherResponse {
        return dto.run {
            CurrentWeatherResponse(
                location = mapLocation(location),
                current = mapCurrentWeather(current)
            )
        }
    }

    fun mapCurrentWeatherResponseBack(response: CurrentWeatherResponse): CurrentWeatherResponseDto {
        return response.run {
            CurrentWeatherResponseDto(
                location = mapLocationBack(location),
                current = mapCurrentWeatherBack(current)
            )
        }
    }


    private fun mapCurrentWeather(weather: CurrentWeatherDto): CurrentWeather {
        return weather.run {
            CurrentWeather(
                cloud = cloud,
                condition = mapCondition(condition),
                feelslike_c = feelslike_c,
                feelslike_f = feelslike_f,
                gust_kph = gust_kph,
                gust_mph = gust_mph,
                humidity = humidity,
                is_day = is_day,
                last_updated = last_updated,
                last_updated_epoch = last_updated_epoch,
                precip_in = precip_in,
                precip_mm = precip_mm,
                pressure_in = pressure_in,
                pressure_mb = pressure_mb,
                temp_c = temp_c,
                temp_f = temp_f,
                uv = uv,
                vis_km = vis_km,
                vis_miles = vis_miles,
                wind_degree = wind_degree,
                wind_dir = wind_dir,
                wind_kph = wind_kph,
                wind_mph = wind_mph
            )
        }
    }

    private fun mapCurrentWeatherBack(weather: CurrentWeather): CurrentWeatherDto {
        return weather.run {
            CurrentWeatherDto(
                cloud = cloud,
                condition = mapConditionBack(condition),
                feelslike_c = feelslike_c,
                feelslike_f = feelslike_f,
                gust_kph = gust_kph,
                gust_mph = gust_mph,
                humidity = humidity,
                is_day = is_day,
                last_updated = last_updated,
                last_updated_epoch = last_updated_epoch,
                precip_in = precip_in,
                precip_mm = precip_mm,
                pressure_in = pressure_in,
                pressure_mb = pressure_mb,
                temp_c = temp_c,
                temp_f = temp_f,
                uv = uv,
                vis_km = vis_km,
                vis_miles = vis_miles,
                wind_degree = wind_degree,
                wind_dir = wind_dir,
                wind_kph = wind_kph,
                wind_mph = wind_mph
            )
        }
    }

    private fun mapCondition(dto: ConditionDto): Condition {
        return dto.run {
            Condition(
                code = code,
                icon = icon,
                text = text
            )
        }
    }

    private fun mapConditionBack(condition: Condition): ConditionDto {
        return condition.run {
            ConditionDto(
                code = code,
                icon = icon,
                text = text
            )
        }
    }

    private fun mapLocation(dto: LocationDto): Location {
        return dto.run {
            Location(
                name = name,
                region = region,
                country = country,
                lat = lat,
                lon = lon,
                tz_id = tz_id,
                localtime_epoch = localtime_epoch,
                localtime = localtime
            )
        }
    }

    private fun mapLocationBack(location: Location): LocationDto {
        return location.run {
            LocationDto(
                name = name,
                region = region,
                country = country,
                lat = lat,
                lon = lon,
                tz_id = tz_id,
                localtime_epoch = localtime_epoch,
                localtime = localtime
            )
        }
    }

}
