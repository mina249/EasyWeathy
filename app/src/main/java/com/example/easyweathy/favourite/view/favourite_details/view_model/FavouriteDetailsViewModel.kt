package com.example.easyweathy.favourite.view.favourite_details.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyweathy.model.GeneralRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.utilities.APIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(val repo: GeneralRepo ):ViewModel() {
    private val weatherResponseDetails: MutableStateFlow<APIState> = MutableStateFlow(APIState.Loading())
    val responseDetails: StateFlow<APIState> = weatherResponseDetails



         fun getWeatherDetailsFromAPI(lat: Double,long: Double,units:String,lang:String){
             viewModelScope.launch(Dispatchers.IO) {

                 repo.getWeatherForHomeScreen(lat,long,units,lang).catch {
                     APIState.Failure(it)
                 }.collect{
                     weatherResponseDetails.value = APIState.Sucess(it)
                     repo.addWeatherToFavourite(it)
                 }

              /*  weatherResponseDetails.postValue(repo.getWeatherForHomeScreen(lat, long,units,lang))
                 repo.addWeatherToFavourite(repo.getWeatherForHomeScreen(lat, long, units, lang))*/
             }
         }




        fun getWeatherDetailsFromRoom(lat: Double,long: Double){
                viewModelScope.launch(Dispatchers.IO){
                    repo.getSelectedFavouriteWeatherDetails(lat,long).collect{
                        weatherResponseDetails.value = APIState.Sucess(it)

                    }
                }
        }






}