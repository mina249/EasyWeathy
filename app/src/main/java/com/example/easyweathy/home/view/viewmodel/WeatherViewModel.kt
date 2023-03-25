package com.example.easyweathy.home.view.viewmodel

import android.content.Context
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

    fun getWeatherResponse(lat:Double,long:Double){
        viewModelScope.launch(Dispatchers.IO){
            weatherResponse.postValue( repo.getWeatherForHomeScreen(lat,long,
                "metric","eng"))
        }
    }

    fun getLocationByGPS(){
        var gps = LocationByGps(context)
        gps.getLastLocation()
        gps.location.observe(context as LifecycleOwner){
            getWeatherResponse(it.first,it.second)
        }

    }


}



