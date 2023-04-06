package com.example.easyweathy.database

import androidx.room.TypeConverter
import com.example.easyweathy.model.Alerts
import com.example.easyweathy.model.Current
import com.example.easyweathy.model.Daily
import com.example.easyweathy.model.Weather
import com.google.gson.Gson

class WeatherConverters {
    /*@TypeConverter
    fun convertCurrentToJson(current: Current) = Gson().toJson(current)
    @TypeConverter
    fun convertJsonToCurrent(value:String) = Gson().fromJson(value,Current::class.java)*/
    @TypeConverter
    fun convertListOfCurrentToJson(listCurrent:List <Current>) = Gson().toJson(listCurrent)
    @TypeConverter
    fun convertJsonToListOfCurrent(value:String) = Gson().fromJson(value,Array<Current>::class.java).toList()
    @TypeConverter
    fun convertListOfDailyToJson(listDaily:List <Daily>) = Gson().toJson(listDaily)
    @TypeConverter
    fun convertJsonToListOfDaily(value:String) = Gson().fromJson(value,Array<Daily>::class.java).toList()
    @TypeConverter
    fun convertListOfWeatherToJson(listWeather: List<Weather>) = Gson().toJson(listWeather)
    @TypeConverter
    fun convertJsonToListOfWeather(value:String) = Gson().fromJson(value,Array<Weather>::class.java).toList()

    @TypeConverter
    fun convertListOfAlertToJson(listAlert: List<Alerts>) = Gson().toJson(listAlert)
    @TypeConverter
    fun convertJsonToListOfAlert(value:String) = Gson().fromJson(value,Array<Alerts>::class.java).toList()



}