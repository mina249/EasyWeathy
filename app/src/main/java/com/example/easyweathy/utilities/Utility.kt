package com.example.easyweathy.utilities

import com.example.easyweathy.R

class Utility {

    companion object{

        fun getImage(icon:String):Int{
            var requireImage:Int = 0
            if (icon == "01d") {
               requireImage =R.drawable.sun
            }
            else if(icon== "01n") {
                requireImage =  R.drawable.moon
            }
            else if(icon == "02d") {
                requireImage =  R.drawable.few_clods
            }
            else if (icon == "02n") {
                requireImage =  R.drawable.night_cloud
            }
            else if (icon == "03d"||icon=="03n"|| icon =="04d"||icon=="04n"){
                requireImage =  R.drawable.scatterd_clouds
            }else if (icon == "09d"||icon == "09n"){
                requireImage = R.drawable.shower_rain
            }else if (icon == "10d"){
                requireImage = R.drawable.day_rain
            }else if (icon=="10n"){
                requireImage = R.drawable.night_rain
            }else if (icon == "11d"||icon == "11n"){
                requireImage = R.drawable.thunderstorm
            }else if (icon == "13d"||icon == "13n"){
                requireImage = R.drawable.snow
            }else{
                requireImage = R.drawable.mist
            }
            return requireImage
        }




    }
}