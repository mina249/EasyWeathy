package com.example.easyweathy.favourite.view.favourite_details

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easyweathy.R
import com.example.easyweathy.databinding.HourlyFavDetailsBinding
import com.example.easyweathy.databinding.HourlyFavDetailsTwoBinding
import com.example.easyweathy.model.Current
import com.example.easyweathy.utilities.Utility
import java.text.SimpleDateFormat
import java.util.*

class HourlyFavDetailsAdapter(var hourly:List<Current>,var context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var bindingFavWeather:HourlyFavDetailsTwoBinding?=null
    var bindingFavWeatherDet:HourlyFavDetailsBinding?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if(viewType == R.layout.hourly_fav_details_two){
            bindingFavWeather = HourlyFavDetailsTwoBinding.inflate(inflater,parent,false)
            HourlyFavHolder(bindingFavWeather!!)
        }else{
            bindingFavWeatherDet =HourlyFavDetailsBinding.inflate(inflater,parent,false)
            HourlyFavHolder2(bindingFavWeatherDet!!)
        }
    }
    override fun getItemCount(): Int {
        return 24
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var shared =context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        val milliSecond=hourly[position].dt
        val hour = Date(milliSecond*1000L)
        val sdf= SimpleDateFormat("h a",Locale.forLanguageTag(shared?.getString("Language","")))
        sdf.timeZone= TimeZone.getDefault()
        val formatedHour=sdf.format(hour)
        val image = Utility.getImage(hourly.get(position).weather?.get(0)?.icon!!)
        if(holder is HourlyFavHolder2){
            holder.binding2.imgHourlyDetails.setImageResource(image)
            holder.binding2?.tvHoursHourlyDetails?.text = formatedHour
            holder.binding2?.tvDegreeHourlyDetails?.text =hourly[position].temp.toString()+Utility.getUnits(context)[1]
        }else if(holder is HourlyFavHolder){
            holder.binding?.tvHoursDailyDet ?.text = formatedHour
            holder.binding?.imgDailyHourDet?.setImageResource(image)
            holder.binding?.tvHourlyDegreeDet?.text = hourly[position].temp.toString()+Utility.getUnits(context)[1]
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            R.layout.hourly_fav_details
        } else {
            R.layout.hourly_fav_details_two
        }

    }


    inner class HourlyFavHolder( val binding: HourlyFavDetailsTwoBinding): RecyclerView.ViewHolder(binding.root)
    inner class HourlyFavHolder2( val binding2: HourlyFavDetailsBinding): RecyclerView.ViewHolder(binding2.root)

}