package com.example.easyweathy.favourite.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.model.GeneralRepo

class FavouriteViewModelFactory  (private val repo: GeneralRepo): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
                FavouriteViewModel(repo) as T
            } else {
                throw java.lang.IllegalArgumentException("view model class not found")
            }
        }
    }
