package com.example.easyweathy.model

import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.database.LocalSource
import com.example.easyweathy.network.RemoteSource
import kotlinx.coroutines.flow.Flow

class ConcreteRepo private constructor(val remoteSource:RemoteSource,val localSource: LocalSource):GeneralRepo {

    companion object{
        private var instance:ConcreteRepo?=null
        fun getInstance( remoteSource: RemoteSource,localSource: LocalSource):ConcreteRepo{
            return instance?: synchronized(this){
                val temp = ConcreteRepo(remoteSource,localSource )
                instance=temp
                temp
            }
        }
    }
    override fun getWeatherForHomeScreen(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
       return remoteSource.getWeatherForHomeScreen(lat,lon,units,language)

    }

    override suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse) {
       localSource.addWeatherToFavourite(weatherResponse)
    }

    override suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>> {
        return localSource.getFavouriteWeather()
    }

    override suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse) {
       localSource.deleteFavouriteWeather(weatherResponse)
    }

    override suspend fun getSelectedFavouriteWeatherDetails(
        lat: Double,
        long: Double
    ): Flow<WeatherResponse> {
        return localSource.getSelectedFavouriteWeatherDetails(lat,long)
    }

    override  fun getCashedHomeWeather(lat: Double, long: Double): Flow<WeatherResponse> {
       return localSource.getCashedHomeWeather(lat,long)
    }

    override suspend fun addAlert(alertPojo: AlertPojo) {
        localSource.addAlert(alertPojo)
    }

    override suspend fun removeAlert(alertPojo: AlertPojo) {
        localSource.removeAlert(alertPojo)
    }

    override suspend fun getAllAlerts(): Flow<List<AlertPojo>> {
      return  localSource.getAllAlerts()
    }
}