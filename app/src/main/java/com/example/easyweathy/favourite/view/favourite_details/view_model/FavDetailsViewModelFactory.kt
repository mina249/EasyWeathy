package com.example.easyweathy.favourite.view.favourite_details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easyweathy.model.GeneralRepo

class FavDetailsViewModelFactory(private val repo: GeneralRepo):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return if (modelClass.isAssignableFrom(FavouriteDetailsViewModel::class.java)){
            FavouriteDetailsViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("view model class not found")
        }
    }


}