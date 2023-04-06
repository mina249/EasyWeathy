package com.example.easyweathy.database

import android.content.Context
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource private constructor(context: Context):LocalSource {

    companion object{
        @Volatile
        private var INSTANCE :ConcreteLocalSource?=null

        fun getInstance(context: Context): ConcreteLocalSource {
            return INSTANCE?: synchronized(this){
                val temp = ConcreteLocalSource(context)
                INSTANCE=temp
                temp
            }
        }
    }

    private val weatherDao: WeatherDao by lazy {
       WeatherDataBase.getInstance(context).getProductDao()
    }

    override suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse) {
        weatherDao.insertWeatherResponse(weatherResponse)
    }

    override suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>> {
        return weatherDao.getAllWeather()
    }

    override suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse) {
            weatherDao.deleteFavouriteResponse(weatherResponse)
    }

    override suspend fun getSelectedFavouriteWeatherDetails(lat:Double,long:Double): Flow<WeatherResponse> {
      return  weatherDao.getFavDetailsWeather(lat,long)
    }

    override  fun getCashedHomeWeather(lat: Double, long: Double): Flow<WeatherResponse> {
      return  weatherDao.getHomeDetailsWeather(lat,long)
    }
}