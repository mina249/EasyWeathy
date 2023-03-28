package com.example.easyweathy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyweathy.model.Weather
import com.example.easyweathy.model.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class), version =4 )
@TypeConverters(WeatherConverters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getProductDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(ctx: Context): WeatherDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,WeatherDataBase::class.java, "weather_dataBase4"
                )
                    .build()
                INSTANCE = instance

                instance
            }

        }
    }
}