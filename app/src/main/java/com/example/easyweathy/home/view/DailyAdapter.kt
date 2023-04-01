package com.example.easyweathy.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easyweathy.R
import com.example.easyweathy.databinding.DailyWeatherBinding
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.utilities.Utility
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var weatherResponse: WeatherResponse) : RecyclerView.Adapter<DailyAdapter.DailyHolder>() {
    lateinit var binding: DailyWeatherBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyWeatherBinding.inflate(inflater,parent,false)
        return DailyHolder(binding)
    }

    override fun getItemCount(): Int {
       return 8
    }

    override fun onBindViewHolder(holder: DailyHolder, position: Int) {
        val milliSecondDate = weatherResponse.daily?.get(position)?.dt
        var date= Date(milliSecondDate?.times(1000L) ?: 0)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar=Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format=SimpleDateFormat("EEEE")
        var day=format.format(calendar.time)
        Log.i("daily position",position.toString())
        binding.tvDay.text = day
        binding.imDaily.setImageResource(R.drawable.sun)
        binding.tvDayDes.text = weatherResponse.daily?.get(position)?.weather?.get(0)?.description
        var minTemp = weatherResponse?.daily?.get(position)?.temp?.max
        var maxTemp = weatherResponse.daily?.get(position)?.temp?.min
        binding.imDaily.setImageResource(Utility.getImage(weatherResponse?.daily?.get(position)?.weather?.get(0)?.icon!!))
        binding.tvDayDegree.text= minTemp.toString() + " / " + maxTemp
    }
    inner class DailyHolder(var binding: DailyWeatherBinding): RecyclerView.ViewHolder(binding.root)
}