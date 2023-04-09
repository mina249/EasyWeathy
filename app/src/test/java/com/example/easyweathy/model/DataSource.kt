package com.example.easyweathy.model

import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.database.LocalSource
import com.example.easyweathy.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataSource:LocalSource,RemoteSource {
        var weatherList : MutableList<WeatherResponse> = mutableListOf()
    fun createTestWeatherResponse():WeatherResponse{
        var weather = Weather(123L,"weatherDescription","01d")
        var temp = Temp(2.0,1.0,3.0,5.0,6.0,9.0)
        var feels = FeelsLike(2.0,3.0,4.0,5.0)
        var daily = Daily(5555666L,6666L,777777L,8888L,999L,7.0,temp,
            feels,5L,6L,5.0,6.0,6L,5.0, listOf(weather),
            6L,5.0,6.0,2.3)
        var alert =Alerts()
        var current = Current(5L,8L,5L,5.5,6.3,3L,6L,7.5,9.0,9L,6L,5.0,9L,5.5, listOf(weather))
       return  WeatherResponse(31.3,32.5,"africa",6L,current, listOf(current), listOf(daily),"status", listOf(alert))

    }

    override suspend fun addWeatherToFavourite(weatherResponse: WeatherResponse) {
        weatherList.add(weatherResponse)
    }

    override suspend fun getFavouriteWeather(): Flow<List<WeatherResponse>> {
       var  weather = weatherList.toList()
        var myFlow = flow {
            emit(weather)
        }
        return myFlow
    }

    override suspend fun deleteFavouriteWeather(weatherResponse: WeatherResponse) {
        weatherList.remove(weatherResponse)
    }

    override suspend fun getSelectedFavouriteWeatherDetails(
        lat: Double,
        long: Double
    ): Flow<WeatherResponse> {
        var response = WeatherResponse()
        weatherList.forEach { weatherResponse ->
            if (weatherResponse.lat == lat && weatherResponse.lon == long) {
                response = weatherResponse
            }
        }
       return flow {
           emit(response)
       }
    }

    override fun getCashedHomeWeather(lat: Double, long: Double): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlert(alertPojo: AlertPojo) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAlert(alertPojo: AlertPojo) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAlerts(): Flow<List<AlertPojo>> {
        TODO("Not yet implemented")
    }

    override fun getWeatherForHomeScreen(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Flow<WeatherResponse> {
      var flowResponse = flow {
          emit(createTestWeatherResponse())
      }
        return flowResponse
    }
}