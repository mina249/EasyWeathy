package com.example.easyweathy.database

import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse)
    suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>>
    suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse)
    suspend fun getSelectedFavouriteWeatherDetails(lat:Double,long:Double):Flow<WeatherResponse>
   fun getCashedHomeWeather(lat: Double,long: Double):Flow<WeatherResponse>

   suspend fun addAlert(alertPojo: AlertPojo)
   suspend fun removeAlert(alertPojo: AlertPojo)
   suspend fun getAllAlerts():Flow<List<AlertPojo>>
}