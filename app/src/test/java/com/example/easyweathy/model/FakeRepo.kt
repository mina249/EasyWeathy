package com.example.easyweathy.model

import com.example.easyweathy.alert.view.AlertPojo
import kotlinx.coroutines.flow.Flow

class FakeRepo:GeneralRepo {
    override fun getWeatherForHomeScreen(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedFavouriteWeatherDetails(
        lat: Double,
        long: Double
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override fun getCashedHomeWeather(lat: Double, long: Double): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlert(alertPojo: AlertPojo) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAlert(alertPojo: AlertPojo) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAlerts(): Flow<List<AlertPojo>> {
        TODO("Not yet implemented")
    }
}