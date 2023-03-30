package com.example.easyweathy.home.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyweathy.model.GeneralRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.utilities.LocationByGps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(val repo: GeneralRepo, val context:Context):ViewModel() {
    private val weatherResponse: MutableLiveData<WeatherResponse> = MutableLiveData()
    val response: LiveData<WeatherResponse> = weatherResponse
           lateinit var lang:String
           lateinit var units:String

    fun getWeatherResponse(lat:Double,long:Double,units:String,lang:String){

        viewModelScope.launch(Dispatchers.IO){

            weatherResponse.postValue( repo.getWeatherForHomeScreen(lat,long,
                units,lang))
        }
    }

    fun getLocationByGPS(lat:Double,long: Double, units:String,lang: String){
       //var gps = LocationByGps(context)
      //  gps.getLastLocation()
        getWeatherResponse(lat,long,units,lang)
       /* gps.location.observe(context as LifecycleOwner){

        }*/
    }
        fun getLocationByMap(units:String,lang: String){
            var shared = context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
            var latitude = shared?.getFloat("lat",0.0f)?.toDouble()
            var logitude = shared?.getFloat("long" , 0.0f)?.toDouble()
            getWeatherResponse(latitude!!,logitude!!,units,lang)
        }

}



