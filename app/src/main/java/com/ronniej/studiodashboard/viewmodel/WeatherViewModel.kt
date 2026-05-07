package com.ronniej.studiodashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronniej.studiodashboard.data.WeatherPeriod
import com.ronniej.studiodashboard.data.WeatherService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val currentWeather: WeatherPeriod) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val weatherService = WeatherService.create()

    init {
        fetchWeatherPeriodically()
    }

    private fun fetchWeatherPeriodically() {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = weatherService.getForecast()
                    val currentWeather = response.properties.periods.firstOrNull()
                    if (currentWeather != null) {
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
