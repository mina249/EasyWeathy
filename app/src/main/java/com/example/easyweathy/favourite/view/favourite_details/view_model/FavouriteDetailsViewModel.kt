package com.example.easyweathy.favourite.view.favourite_details.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyweathy.model.GeneralRepo
import com.example.easyweathy.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(val repo: GeneralRepo ):ViewModel() {
    private val weatherResponseDetails: MutableLiveData<WeatherResponse> = MutableLiveData()
    val responseDetails: LiveData<WeatherResponse> = weatherResponseDetails



         fun getWeatherDetailsFromAPI(lat: Double,long: Double){
             viewModelScope.launch(Dispatchers.IO) {
                weatherResponseDetails.postValue(repo.getWeatherForHomeScreen(lat, long, "metric", "eng"))
                 repo.addWeatherToFavourite(repo.getWeatherForHomeScreen(lat, long, "metric", "eng"))
             }
         }




        fun getWeatherDetailsFromRoom(lat: Double,long: Double){
                viewModelScope.launch(Dispatchers.IO){
                    repo.getSelectedFavouriteWeatherDetails(lat,long).collect{
                        weatherResponseDetails.postValue(it)
                        Log.i("GedoMilad",it.timezone)
                    }
                }
        }






}