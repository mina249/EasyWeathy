package com.example.easyweathy.home.view.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easyweathy.model.GeneralRepo

class WeatherViewModelFactory(private val repo:GeneralRepo,private val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
          WeatherViewModel(repo,context) as T
        }else{
            throw java.lang.IllegalArgumentException("view model class not found")
        }
    }

}