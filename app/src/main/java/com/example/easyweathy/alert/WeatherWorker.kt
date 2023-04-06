package com.example.easyweathy.alert

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.RemoteSource
import kotlinx.coroutines.flow.collect

class WeatherWorker(appContext:Context, workerParameters: WorkerParameters) : CoroutineWorker(appContext,workerParameters) {
    @SuppressLint("SuspiciousIndentation")
    override suspend fun doWork(): Result {
        var weather:WeatherResponse?=null
        var repo = ConcreteRepo.getInstance(ConcreteRemoteSource,ConcreteLocalSource.getInstance(applicationContext))
          repo.getWeatherForHomeScreen(31.5,31.6,"metric","en").collect{
              weather = it
          }

       Log.i("Jesus",weather?.timezone!!)
       createNotificationChannel(weather!!.timezone)
        return Result.success(workDataOf("A" to weather!!.timezone))
    }

    @SuppressLint("WrongConstant")
    private fun createNotificationChannel(name:String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "Mina Nageh"
            val description = "Cloudy Sky"
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel("Easy Weathy",name,importance)
            channel.description = description
            val notificationManager = applicationContext?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)

        }
        var intent = Intent()
        val i = Intent(applicationContext, AlertFragment::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, i, 0)
        val builder = NotificationCompat.Builder(applicationContext, "Easy Weathy")
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(name)
            .setContentText("Alarm")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
        val notificationManger = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManger.notify(123, builder.build())
    }



}
