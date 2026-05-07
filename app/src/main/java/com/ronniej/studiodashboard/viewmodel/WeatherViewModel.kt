package com.ronniej.studiodashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronniej.studiodashboard.data.WeatherService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class CurrentWeather(
    val temperature: Int,
    val temperatureUnit: String,
    val description: String,
    val iconUrl: String
)

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val currentWeather: CurrentWeather) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val weatherService = WeatherService.create()

    // Coordinates for Creswell, OR and API details
    private val lat = 43.9176
    private val lon = -123.0245
    private val appId = "80f01594cb31d650aa6aa8c4f4dd794d"
    private val units = "imperial"

    init {
        fetchWeatherPeriodically()
    }

    private fun fetchWeatherPeriodically() {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = weatherService.getWeather(lat, lon, appId, units)
                    val weatherData = response.weather.firstOrNull()
                    if (weatherData != null) {
                        val iconUrl = "https://openweathermap.org/img/wn/${weatherData.icon}@2x.png"
                        val currentWeather = CurrentWeather(
                            temperature = response.main.temp.roundToInt(),
                            temperatureUnit = "F",
                            description = weatherData.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                            iconUrl = iconUrl
                        )
                        _weatherState.value = WeatherState.Success(currentWeather)
                    } else {
                        _weatherState.value = WeatherState.Error("No forecast data available")
                    }
                } catch (e: Exception) {
                    _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")
                }
                // Refresh every 15 minutes
                delay(15 * 60 * 1000L)
            }
        }
    }
}
