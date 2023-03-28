package com.example.easyweathy.database

import androidx.room.*
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM Weather_table")
     fun getAllWeather(): Flow<List<WeatherResponse>>

    @Delete
    suspend fun deleteFavouriteRespose(weatherResponse: WeatherResponse)
}