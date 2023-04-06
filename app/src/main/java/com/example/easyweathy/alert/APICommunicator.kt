package com.example.easyweathy.alert

import com.example.easyweathy.model.WeatherResponse

interface APICommunicator {
    fun callAPI():WeatherResponse
}