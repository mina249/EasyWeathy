package com.example.easyweathy.alert.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater

import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import kotlinx.coroutines.*



class WeatherReciever:BroadcastReceiver() {
     var weatherResponse: WeatherResponse?=null
private var flag = 0


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context, intent: Intent?) {
        val prefrenceList = getPrefrenceData(context)
        val lat = intent?.getDoubleExtra("lat", 0.0)
        val long = intent?.getDoubleExtra("long", 0.0)
        val id = intent?.getIntExtra("id", 0)
        val type = intent?.getStringExtra("type")

        val repo =
            ConcreteRepo.getInstance(ConcreteRemoteSource, ConcreteLocalSource.getInstance(context))
        if (prefrenceList[2] == "yes") {
            if (NetWorkChecker.getConnectivity(context)!!) {
                CoroutineScope(Dispatchers.IO).launch {
                    repo.getWeatherForHomeScreen(lat!!, long!!, prefrenceList[0], prefrenceList[1])
                        .collect {
                            weatherResponse = it
                            if (type == "Alert") {
                                CoroutineScope(Dispatchers.Main).launch {
                                    flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

                                    } else {
                                        WindowManager.LayoutParams.TYPE_PHONE
                                    }
                                    setAlarmOverApps(context, weatherResponse!!)
                                }
                            } else {

                                val i = Intent(context, AlertFragment::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
                                val builder = NotificationCompat.Builder(context, id.toString())
                                    .setSmallIcon(R.drawable.app_logo)
                                    .setContentTitle(weatherResponse?.timezone)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setContentIntent(pendingIntent)
                                if (!weatherResponse?.alerts?.isNullOrEmpty()!!) {
                                    builder.setContentText(weatherResponse?.alerts!![0].description)
                                } else {
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
            }else {
                val i = Intent(context, AlertFragment::class.java)
                intent?.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
                val builder = NotificationCompat.Builder(context, id.toString())
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle("No internet")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                builder.setContentText("cannot show alert data please connect to internet")
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
    @SuppressLint("SetTextI18n", "InflateParams")
    private suspend fun setAlarmOverApps(context: Context, weatherResponse: WeatherResponse) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.alert_over_apps, null, false)
        val dismiss = view.findViewById(R.id.over_app_dismiss) as Button
        val alertDescription = view.findViewById(R.id.over_app_tv) as TextView
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
               flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        layoutParams.gravity = Gravity.CENTER

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            if (weatherResponse.alerts.isNullOrEmpty()){
                alertDescription.text = "The weather To day is very good ,Have a nice day"
            }else {
                alertDescription.text = weatherResponse.alerts?.get(0)?.description
            }
        }
        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        dismiss.setOnClickListener {
            mediaPlayer?.release()
            windowManager.removeView(view)
        }
    }

    private fun getPrefrenceData(context: Context): MutableList<String> {
        val shared = context.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)

        return mutableListOf(

            shared?.getString("Units", "metric")!!,
            shared.getString("Language", "en")!!,
            shared.getString("isAlert", "yes")!!
        )
    }



}

class DeleteNotificationReciever:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val alertId=intent?.getIntExtra("alert",0)
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(alertId!!)
    }


}
