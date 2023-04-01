package com.example.easyweathy.favourite.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.GeneralRepo
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FavouriteViewModel(val repo:GeneralRepo):ViewModel() {

  private val favouriteWeatherList : MutableLiveData<List<WeatherResponse>> = MutableLiveData()
    val _favouriteWeatherList : LiveData<List<WeatherResponse>> = favouriteWeatherList

        fun insertFavouriteWeather(lat:Double,long: Double,units:String,lang:String){
            viewModelScope.launch(Dispatchers.IO) {

                repo.getWeatherForHomeScreen(lat,long,units,lang).collect{
                    repo.addWeatherToFavourite(it)
                }

            }
        }

        fun deleteFromFavourite(weatherResponse: WeatherResponse){
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteFavouriteWeather(weatherResponse)
            }
        }



    fun getAllFavWeather(){
        viewModelScope.launch (Dispatchers.IO) {
            repo.getFavouriteWeather().collect {
                favouriteWeatherList.postValue(it)

            }




        }
    }
  /*  suspend fun getWeatherFromApi(lat:Double, long:Double):WeatherResponse{
        return repo.getWeatherForHomeScreen(lat,long,"metric","en")

    }*/



}