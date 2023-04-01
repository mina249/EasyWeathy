package com.example.easyweathy.network

import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


interface RemoteSource {
    fun getWeatherForHomeScreen(lat:Double , lon:Double, units:String,language:String):Flow<WeatherResponse>
}