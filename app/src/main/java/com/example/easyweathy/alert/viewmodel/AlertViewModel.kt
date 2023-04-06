package com.example.easyweathy.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.GeneralRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlertViewModel( val repo: GeneralRepo):ViewModel() {
    private val alertList : MutableLiveData<List<AlertPojo>> = MutableLiveData()
    val _alertList : LiveData<List<AlertPojo>> = alertList


    fun deleteAlert(alertPojo: AlertPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeAlert(alertPojo)

        }
    }
    fun addAlert(alertPojo: AlertPojo){
        viewModelScope.launch(Dispatchers.IO){
            repo.addAlert(alertPojo)
        }
    }

    fun getAlerts(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getAllAlerts().collect{
                alertList.postValue(it)
            }
        }
    }




}