package com.example.easyweathy.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easyweathy.favourite.view_model.FavouriteViewModel
import com.example.easyweathy.model.GeneralRepo

class AlertViewModelFactory  (private val repo: GeneralRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
          AlertViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("view model class not found")
        }
    }
}