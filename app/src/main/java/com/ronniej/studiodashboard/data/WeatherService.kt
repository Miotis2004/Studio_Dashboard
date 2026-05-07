package com.ronniej.studiodashboard.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class WeatherForecastResponse(
    val properties: WeatherProperties
)

data class WeatherProperties(
    val periods: List<WeatherPeriod>
)

data class WeatherPeriod(
    val temperature: Int,
    val temperatureUnit: String,
    val shortForecast: String,
    val icon: String
)

interface WeatherService {
    // Hardcoded URL for Creswell, OR as per requirements
    @GET("gridpoints/PQR/86,33/forecast")
    suspend fun getForecast(): WeatherForecastResponse

    companion object {
        private const val BASE_URL = "https://api.weather.gov/"

        fun create(): WeatherService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherService::class.java)
        }
    }
}
