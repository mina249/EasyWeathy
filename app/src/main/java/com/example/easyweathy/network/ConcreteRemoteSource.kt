package com.example.easyweathy.network

import android.widget.Toast
import com.example.easyweathy.model.WeatherResponse


object ConcreteRemoteSource : RemoteSource {
        lateinit var myData:WeatherResponse
    override suspend fun getWeatherForHomeScreen(lat:Double , lon:Double, units:String,language:String): WeatherResponse{

        val myResponse = Service.retrofitService.getWeatherResponse(lat,lon,units,language)
        if(myResponse.isSuccessful) {
            myData = myResponse.body()!!


        }
        return myData
    }
}
object Service{
    val retrofitService: API by lazy {
        RetrofitHelper.retrofit.create(API::class.java)
    }
}