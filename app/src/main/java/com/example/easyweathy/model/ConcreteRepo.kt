package com.example.easyweathy.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.easyweathy.network.RemoteSource

class ConcreteRepo private constructor(val remoteSource:RemoteSource):GeneralRepo {

    companion object{
        private var instance:ConcreteRepo?=null
        fun getInstance( remoteSource: RemoteSource):ConcreteRepo{
            return instance?: synchronized(this){
                val temp = ConcreteRepo(remoteSource)
                instance=temp
                temp
            }
        }
    }
    override suspend fun getWeatherForHomeScreen(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): WeatherResponse {
       return remoteSource.getWeatherForHomeScreen(lat,lon,units,language)

    }
}