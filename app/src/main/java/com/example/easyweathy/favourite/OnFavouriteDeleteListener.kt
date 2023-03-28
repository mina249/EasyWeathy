package com.example.easyweathy.favourite

import com.example.easyweathy.model.WeatherResponse

interface OnFavouriteDeleteListener {
    fun deleteWeatherFromFavourite(weatherResponse: WeatherResponse)
}