package com.example.easyweathy.alert

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.database.LocalSource
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.GeneralRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.RemoteSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class WeatherReciever():BroadcastReceiver() {
    lateinit var weatherResponse: WeatherResponse


    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context, intent: Intent?) {
        var repo =
            ConcreteRepo.getInstance(ConcreteRemoteSource, ConcreteLocalSource.getInstance(context))
        var result = repo.getWeatherForHomeScreen(-40.900557, 174.885971, "metric", "en")

            var id = intent?.getIntExtra("id",0)

        CoroutineScope(Dispatchers.IO).launch {
            result.collect {
                weatherResponse = it
                val i = Intent(context, AlertFragment::class.java)
                intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
                val builder = NotificationCompat.Builder(context, id.toString())
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(weatherResponse.timezone)

                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                if (!weatherResponse?.alerts?.isNullOrEmpty()!!){
                    builder.setContentText(weatherResponse.alerts!![0].description)
                }else{
                    builder.setContentText("The weather To day is very good ,Have a nice day")
                }
                val notificationManger = NotificationManagerCompat.from(context)

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {


                }
                notificationManger.notify(123, builder.build())
            }

        }
        }



    }
