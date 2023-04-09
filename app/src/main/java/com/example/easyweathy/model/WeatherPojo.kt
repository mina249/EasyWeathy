package com.example.easyweathy.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Weather_Table", primaryKeys = ["lat","lon"])

data class WeatherResponse (


    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    @Embedded
    val current: Current?,
    val hourly: List<Current>?,
    val daily: List<Daily>?,
    var status:String,
    var alerts: List<Alerts>?,

): Serializable{
    constructor():this(0.0,0.0,"",0,null, listOf(), listOf(),"home", listOf())
}

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,

    val temp: Double,
    val feelsLike: Double,

    val pressure: Long,
    val humidity: Long,

    val dewPoint: Double,
    val uvi: Double,

    val clouds: Long,
    val visibility: Long,

    val wind_speed: Double,

    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>?,
){
    constructor():this(0,0, 0,0.0,0.0, 0,0, 0.0,0.0, 0,0, 0.0,0,0.0, listOf())
}


data class Weather (
    val id: Long,
    val description: String,
    val icon: String
)

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double,
    val rain: Double? = null
)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)
data class Alerts(
    var senderName: String? = null,
    var event: String? = null,
    var start: Int? = null,
    var end: Int? = null,
    var description: String? = null,
    var tags: List<String> = arrayListOf()
){
    constructor():this(null,null,null,null,null, listOf())
}
