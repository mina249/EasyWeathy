package com.example.easyweathy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class, AlertPojo::class), version = 24 )
@TypeConverters(WeatherConverters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getProductDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(ctx: Context): WeatherDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,WeatherDataBase::class.java, "weather_dataBase24"
                )
                    .build()
                INSTANCE = instance

                instance
            }

        }
    }
}