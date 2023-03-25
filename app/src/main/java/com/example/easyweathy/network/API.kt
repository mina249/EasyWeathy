package com.example.easyweathy.network

import android.database.Observable
import com.example.easyweathy.model.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("onecall")
    suspend fun getWeatherResponse (@Query("lat") lat:Double
                                    , @Query("lon") lon: Double,
                                    @Query("units") units:String,
                                    @Query("lang") language:String,
                                    @Query("appid") appid:String = "d6a347a6872fc132aad35e4ed9e1f138" ):Response<WeatherResponse>

}