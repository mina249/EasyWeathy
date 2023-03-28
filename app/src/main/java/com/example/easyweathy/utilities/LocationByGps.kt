package com.example.easyweathy.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

private const val MAP_PERMISSION_ID  = 505
class LocationByGps constructor(var context: Context){

    private var mutableLocation: MutableLiveData<Pair<Double,Double>> = MutableLiveData()
    var fusedLocationClient   = LocationServices.getFusedLocationProviderClient(context)
    var location : LiveData<Pair<Double,Double>> = mutableLocation
    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(checkPermission()){
            if(isPermissionEnabled()){
                requestNewLocation()
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)

            }
        }else{
            requestPermission()
        }

    }

    fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionEnabled():Boolean{
        var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    private fun requestNewLocation() {
        val locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = LocationRequest.QUALITY_HIGH_ACCURACY
        locationRequest.interval = 0

            fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.myLooper()
        )
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION), MAP_PERMISSION_ID)
    }

    private val locationCallBack : LocationCallback = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val lastLocation: Location = locationResult.lastLocation
            Log.i("mina",lastLocation.latitude.toString())
            mutableLocation.postValue(Pair(lastLocation.latitude,lastLocation.longitude))
            stopLocationUpdate()



        }

    }

    fun stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

}