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
import com.example.easyweathy.utilities.APIState
import com.example.easyweathy.utilities.LocationByGps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherViewModel(val repo: GeneralRepo, val context:Context):ViewModel() {
    private val weatherResponse: MutableStateFlow<APIState> = MutableStateFlow(APIState.Loading())
    val response: StateFlow<APIState> = weatherResponse
           lateinit var lang:String
           lateinit var units:String

    fun getWeatherResponse(lat:Double,long:Double,units:String,lang:String){

        viewModelScope.launch(Dispatchers.IO){
            repo.getWeatherForHomeScreen(lat,long,units,lang)
                .catch {
                   weatherResponse.value = APIState.Failure(it)
                 }.collect{
                    it.status = "home"
                    repo.addWeatherToFavourite(it)
                    weatherResponse.value = APIState.Sucess(it)
                }
        }
    }

    fun getLocationByGPS(lat:Double,long: Double, units:String,lang: String){
        getWeatherResponse(lat,long,units,lang)
    }
        fun getLocationByMap(units:String,lang: String){
            var shared = context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
            var latitude = shared?.getFloat("lat",0.0f)?.toDouble()
            var logitude = shared?.getFloat("long" , 0.0f)?.toDouble()
            getWeatherResponse(latitude!!,logitude!!,units,lang)
        }


}






