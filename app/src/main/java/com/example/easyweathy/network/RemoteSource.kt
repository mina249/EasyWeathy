package com.example.easyweathy.network

import com.example.easyweathy.model.WeatherResponse


interface RemoteSource {
   suspend fun getWeatherForHomeScreen(lat:Double , lon:Double, units:String,language:String):WeatherResponse
}