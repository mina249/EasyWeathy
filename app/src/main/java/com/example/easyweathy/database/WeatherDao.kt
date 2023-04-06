package com.example.easyweathy.database

import androidx.room.*
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM Weather_table WHERE status = 'fav' ")
     fun getAllWeather(): Flow<List<WeatherResponse>>
     
     @Query("SELECT * FROM Weather_table WHERE lat LIKE :latitude AND lon LIKE :longtuide AND status = 'fav' ")
     fun getFavDetailsWeather(latitude:Double,longtuide:Double):Flow<WeatherResponse>

    @Delete
    suspend fun deleteFavouriteResponse(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM Weather_table WHERE lat LIKE :latitude AND lon LIKE :longtuide AND status = 'home' ")
    fun getHomeDetailsWeather(latitude:Double,longtuide:Double):Flow<WeatherResponse>



}