package com.example.easyweathy.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easyweathy.R
import com.example.easyweathy.databinding.HourlyWeatherBinding
import com.example.easyweathy.databinding.HourlyWeatherHomeBinding
import com.example.easyweathy.model.Current
import com.example.easyweathy.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(var hourly:List<Current>):
    RecyclerView.Adapter<ViewHolder>() {
    var bindingWeather:HourlyWeatherHomeBinding?=null
    var bindingWeather2:HourlyWeatherBinding?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if(viewType == R.layout.hourly_weather_home){
            bindingWeather = HourlyWeatherHomeBinding.inflate(inflater,parent,false)
            HourlyHolder(bindingWeather!!)
        }else{
            bindingWeather2 = HourlyWeatherBinding.inflate(inflater,parent,false)
            HourlyHolder2(bindingWeather2!!)
        }
    }
    override fun getItemCount(): Int {
        return hourly.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val milliSecond=hourly[position].dt
        val hour =Date(milliSecond*1000L)
        val sdf= SimpleDateFormat("h a")
        sdf.timeZone=TimeZone.getDefault()
        val formatedHour=sdf.format(hour)
        Log.i("test",hourly.toString())
        Log.i("test",position.toString())
        if(holder is HourlyHolder2){

            holder.binding2?.tvHoursHourly?.text = formatedHour
            holder.binding2?.tvDegreeHourly?.text =hourly[position].temp.toString()
        }else if(holder is HourlyHolder){
            holder.binding?.tvHoursDaily ?.text = formatedHour
            holder.binding?.imgDailyHour?.setImageResource(R.drawable.sun)
            holder.binding?.tvHourlyDegree?.text = hourly[position].temp.toString()
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            R.layout.hourly_weather
        } else {
            R.layout.hourly_weather_home

        }

    }

    inner class HourlyHolder( val binding:HourlyWeatherHomeBinding): ViewHolder(binding.root)
    inner class HourlyHolder2( val binding2:HourlyWeatherBinding): ViewHolder(binding2.root)
}