package com.example.easyweathy.network

import android.widget.Toast
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object ConcreteRemoteSource : RemoteSource {
        lateinit var myData:WeatherResponse
    override  fun getWeatherForHomeScreen(lat:Double , lon:Double, units:String,language:String): Flow<WeatherResponse>{
        var flow = flow {
            val myResponse = Service.retrofitService.getWeatherResponse(lat,lon,units,language)
            if(myResponse.isSuccessful) {
                  myData = myResponse.body()!!
                  emit(myData)
            }
        }
        return flow
    }
}
object Service{
    val retrofitService: API by lazy {
        RetrofitHelper.retrofit.create(API::class.java)
    }
}