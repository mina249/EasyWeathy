package com.example.easyweathy.utilities

import com.example.easyweathy.model.WeatherResponse

sealed class APIState {
    class Sucess(val weatherResponse: WeatherResponse):APIState()
    class Failure(val msg:Throwable):APIState()
    class Loading():APIState()
}