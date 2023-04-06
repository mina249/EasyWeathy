package com.example.easyweathy.favourite.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easyweathy.R
import com.example.easyweathy.databinding.FavouriteCardBinding
import com.example.easyweathy.databinding.FragmentFavouriteBinding
import com.example.easyweathy.favourite.OnCardFavClickListener
import com.example.easyweathy.favourite.OnFavouriteDeleteListener
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.utilities.Utility
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Handler

class FavouriteAdapter(var favList:List<WeatherResponse>,var listener:OnFavouriteDeleteListener,var context: Context,var cardlistener:OnCardFavClickListener) :RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder>() {
    lateinit var binding: FavouriteCardBinding
    lateinit var dialode: Dialog
    lateinit var weatherResponse: WeatherResponse


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavouriteCardBinding.inflate(inflater, parent, false)
        return FavouriteHolder(binding)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: FavouriteHolder, position: Int) {

        weatherResponse = favList[position]
        var milliSecondDate = weatherResponse.current?.dt
        Log.i("time", milliSecondDate.toString())
        var date = Date(milliSecondDate?.times(1000L) ?: 0)
        val timeZoneDate = SimpleDateFormat("dd  MMM , hh : mm a ")
        var formatedDate = timeZoneDate.format(date)
        var img = weatherResponse.current?.weather?.get(0)?.icon
        holder.binding.imgFav.setImageResource(Utility.getImage(img!!))
        holder.binding.tvFavDay.text = formatedDate
        holder.binding.tvCountryFav.text = weatherResponse.timezone
        holder.binding.tvFavDegree.text = weatherResponse.hourly?.get(position)?.temp.toString()
        holder.binding.tvFavStatus.text = weatherResponse.current?.weather?.get(0)?.description
        holder.binding.deleteLottie.setOnClickListener() {
            var dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.delete_from_favourite)
            val window: Window? = dialog.getWindow()
            window?.setLayout(
                Constraints.LayoutParams.MATCH_PARENT,
                Constraints.LayoutParams.WRAP_CONTENT
            )
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()


            dialog.findViewById<Button>(R.id.btn_delete_fav).setOnClickListener {
                dialog.dismiss()
               holder.binding.deleteLottie.playAnimation()
                android.os.Handler().postDelayed({
                    listener.deleteWeatherFromFavourite(favList[position])
                }, 2500)


            }
            dialog.findViewById<Button>(R.id.btn_cancel_fav).setOnClickListener() {
                dialog.dismiss()
            }

        }
            var lat = weatherResponse.lat
            var long = weatherResponse.lon
            holder.binding.favouriteCardView.setOnClickListener() {
                cardlistener.showFavouriteDetails(lat, long)
            }

    }
    inner class FavouriteHolder(var binding: FavouriteCardBinding) :
        ViewHolder(binding.root)
    /*fun dialogDeleteConfirmation() {
    }*/
}



