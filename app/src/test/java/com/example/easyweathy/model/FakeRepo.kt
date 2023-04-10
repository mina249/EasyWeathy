package com.example.easyweathy.model

import androidx.lifecycle.MutableLiveData
import com.example.easyweathy.alert.view.AlertPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo():GeneralRepo {
    var weatherList: MutableList<WeatherResponse> = mutableListOf()
    var alert:MutableList<AlertPojo> = mutableListOf()

    override fun getWeatherForHomeScreen(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse) {
        weatherList.add(weatherResponse)
    }

    override suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>> {
        val flowWeather= flow {
            val data =weatherList.toList()
            if(data!=null){
                emit(data)
            }
        }
        return flowWeather
    }

    override suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse) {
       weatherList.remove(weatherResponse)
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
        alert.add(alertPojo)
    }

    override suspend fun removeAlert(alertPojo: AlertPojo) {
        alert.remove(alertPojo)
    }

    override suspend fun getAllAlerts(): Flow<List<AlertPojo>> {
     var myAlerts = alert.toList()
        return flow {
            emit(myAlerts)
        }
    }
}