package com.example.easyweathy.utilities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.location.Geocoder
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import com.example.easyweathy.R
import java.util.*

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
        fun getAdressName(lat:Double,long:Double,context: Context):String?{


            try {
                var geocoder = Geocoder(context, Locale.getDefault())

                var  address = geocoder.getFromLocation(lat,long,1)
                if(address.isNullOrEmpty()){
                    return "invalid";
                }else{
                    var loc : String =""
                    loc = address[0].adminArea + ",\n"+ address[0].countryName
                    return loc
                }

            }catch (e:java.lang.Exception){
                return "invalid"
            }


        }

        fun getUnits(context: Context):MutableList<String>{
            var prefList= mutableListOf<String>()
            var shared =context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
            var units = shared?.getString("Units","standard")
            var lang = shared?.getString("Language","en")
            if(lang=="en"){
                if(units == "metric"){
                    prefList.add(" m/sec")
                    prefList.add(" °C")

                }else if (units == "imperial"){
                    prefList.add(" miles/h")
                    prefList.add(" °F")
                }else if(units == "standard"){
                    prefList.add(" m/sec")
                    prefList.add(" °K")
                }

            }else{
                if(units == "metric"){
                    prefList.add(" م/ ث")
                    prefList.add(" °س")

                }else if (units == "imperial"){
                    prefList.add("ميل/ساعة")
                    prefList.add(" °ف")
                }else{
                    prefList.add(" م/ ث")
                    prefList.add(" °ك")
                }


            }
                return prefList

        }

        @SuppressLint("SuspiciousIndentation")
        fun noInternetDialog(context: Context) {
                var dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.no_internet)
                val window: Window? = dialog.getWindow()
                window?.setBackgroundDrawableResource(R.color.transparent);
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
                window?.setBackgroundDrawableResource(R.color.transparent);
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
                 var cancel =   dialog.findViewById<Button>(R.id.no_net_cancel)
                    cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                }

            }
        }



