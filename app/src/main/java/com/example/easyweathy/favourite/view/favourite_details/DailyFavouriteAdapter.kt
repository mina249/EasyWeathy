package com.example.easyweathy.favourite.view.favourite_details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easyweathy.R
import com.example.easyweathy.databinding.DailyDetailsBinding
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.utilities.Utility
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class DailyFavouriteAdapter(var weatherResponse: WeatherResponse,var context: Context):RecyclerView.Adapter<DailyFavouriteAdapter.DailyFavHolder>() {
    lateinit var binding: DailyDetailsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyFavHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyDetailsBinding.inflate(inflater,parent,false)
        return DailyFavHolder(binding)
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: DailyFavHolder, position: Int) {
        val milliSecondDate = weatherResponse.daily?.get(position)?.dt
        var date= Date(milliSecondDate?.times(1000L) ?: 0)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format= SimpleDateFormat("EEEE")
        var day=format.format(calendar.time)
        val image = Utility.getImage(weatherResponse.current?.weather?.get(0)?.icon!!)
        binding.imDailyDet.setImageResource(image)
        binding.tvDayDet.text = day
        binding.tvDayDesDet.text = weatherResponse.daily?.get(position)?.weather?.get(0)?.description
        var minTemp = weatherResponse?.daily?.get(position)?.temp?.max
        var maxTemp = weatherResponse.daily?.get(position)?.temp?.min
        binding.tvDayDegreeDet.text= minTemp.toString() + " / " + maxTemp + Utility.getUnits(context)[1]
    }

    inner class DailyFavHolder(var binding: DailyDetailsBinding): RecyclerView.ViewHolder(binding.root)
}