package com.example.easyweathy.alert.view

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.work.*
import com.airbnb.lottie.LottieAnimationView
import com.example.easyweathy.OnAlertDeleteListener
import com.example.easyweathy.R
import com.example.easyweathy.alert.viewmodel.AlertViewModel
import com.example.easyweathy.alert.viewmodel.AlertViewModelFactory
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentAlarmBinding
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.network.ConcreteRemoteSource
import com.google.android.gms.maps.model.LatLng
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*



class AlertFragment : Fragment(),OnAlertDeleteListener{
    private lateinit var alarmManager: AlarmManager
  private  lateinit var pendingIntent: PendingIntent
    private lateinit var binding: FragmentAlarmBinding
    private var selectedFromDay = 0
    private var selectedFromMonth = 0
    private  var selectedFromYear = 0
    private  var selectedFromHour = 0
    private  var selectedFromMinutes = 0
    private var selectedToDay = 0
    private  var selectedToMonth = 0
    private var selectedToYear = 0
    private var selectedToHour = 0
    private var selectedToMinutes = 0
    private lateinit var startDate:TextView
    private lateinit var endDate :TextView
    private var startCalender = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private var startDatePicker:DatePickerDialog.OnDateSetListener?=null
    private  var endDatePicker:DatePickerDialog.OnDateSetListener?=null
    private lateinit var startTimePicker:TimePickerDialog.OnTimeSetListener
    private lateinit var endTimePicker:TimePickerDialog.OnTimeSetListener
    private  var alertType:String ="Alert"
    private var startMilli:Long=0
    private var endMilli:Long=0
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertFactory:AlertViewModelFactory
    private var alertadapter: AlertAdapter?=null
    private var alertManger :LayoutManager?=null
    private  var selectedAlert:String = "Rain"
    private var alert:AlertPojo?=null
    private var location:LatLng?=null
    private  var numberOfDays = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(inflater,container,false)
        val actionBar = activity?.actionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.alert)
        alertManger = LinearLayoutManager(context)
        return binding.root
    }

   @SuppressLint("WrongConstant")
   private fun createNotificationChannel(id:Int){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val name: CharSequence = "easyWeathy"
           val description = "weather"
           val importance = NotificationManager.IMPORTANCE_MAX
           val channel = NotificationChannel(id.toString(),name,importance)
           channel.description = description
           val notificationManager = activity?.getSystemService(NotificationManager::class.java)
           notificationManager?.createNotificationChannel(channel)
       }
   }

    private fun setAlarm(start:Long,id:Int){
         numberOfDays = (selectedToDay - selectedFromDay) + ((selectedToMonth - selectedFromMonth)*30)
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), WeatherReciever::class.java)
        intent.putExtra("lat", location?.latitude)
        intent.putExtra("long", location?.longitude)
        intent.putExtra("type", alertType)
        val dayInMilli = 24*60*60*1000
        for (i in 0..numberOfDays) {
            intent.putExtra("id", id+i)
            pendingIntent = getBroadcast(requireContext(), id+i, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, start+(i*dayInMilli), pendingIntent)
            deleteCompletedNotification(alert!!,i,endMilli)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showAlertDialoge(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_dialoge)
        val window: Window? = dialog.window
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        if (!Settings.canDrawOverlays(requireContext())){
            requestOverAppPermission()
        }

        startDate = dialog.findViewById(R.id.tv_start_date)
        endDate = dialog.findViewById(R.id.tv_end_date)
        val lotie = dialog.findViewById<LottieAnimationView>(R.id.alarm_lottie)
        lotie?.repeatCount = Int.MAX_VALUE
     startDateButton = dialog.findViewById(R.id.btn_calendar_from)
     startDateButton.setOnClickListener {
        setSelectedStartDate()
     }
        endDateButton =dialog.findViewById(R.id.btn_calendar_to)
        endDateButton.setOnClickListener {
          setSelectedEndDate()
        }
        dialog.findViewById<RadioButton>(R.id.rb_alertdialogue_alert).setOnClickListener {
            alertType = "Alert"
        }
        dialog.findViewById<RadioButton>(R.id.rb_notification_alertdialogue).setOnClickListener {
            alertType = "Notification"
        }
        val autoCompleteDp = dialog.findViewById<AutoCompleteTextView>(R.id.alert_drop_down)
        val alertItems = resources.getStringArray(R.array.myAlerts)
        val alertAdapter = ArrayAdapter(requireContext(),R.layout.alerts_dp,alertItems)
        autoCompleteDp.setAdapter(alertAdapter)
        autoCompleteDp.setOnItemClickListener { parent, view, position, id ->
            selectedAlert = parent.getItemAtPosition(position).toString()
        }

        val save = dialog.findViewById<Button>(R.id.btn_save_alert_dialogue)
        save.setOnClickListener {
            val id = createAlertId(startDate.text.toString(),endDate.text.toString(),alertType)
            location = getLocationFromPrefrence()
            if( startDate.text.isNullOrEmpty()||endDate.text.isNullOrEmpty()) {
                Toast.makeText(context, "You must choose start and end date", Toast.LENGTH_SHORT)
                    .show()
            }else if( startCalender.time > endCalendar.time) {

                Toast.makeText(context, "End date is earlier than start date", Toast.LENGTH_SHORT)
                    .show()


            } else if(startMilli < Date().time){
                Toast.makeText(
                    context,
                    "Start Date is already passed, please choose valid Date",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }else {
                alert = AlertPojo(startDate.text.toString(),endDate.text.toString(), alertType,
                    location!!.latitude, location!!.longitude,
                    selectedAlert,id)
                startMilli = startCalender.timeInMillis
                createNotificationChannel(id)
                setAlarm(startMilli, id)
                alertViewModel.addAlert(alert!!)
                alertadapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun setDateText(c:Calendar, date:TextView){
        date.text = SimpleDateFormat("dd,MMM,yy \n hh:mm aa").format(c.time)

    }
    private fun setSelectedStartDate() {
       startTimePicker= TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            startCalender.set(Calendar.HOUR,hourOfDay)
            startCalender.set(Calendar.MINUTE,minute)
           selectedFromHour = hourOfDay
           selectedFromMinutes = minute
            setDateText(startCalender,startDate)
        }

        startDatePicker=  DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            startCalender.set(Calendar.YEAR,year)
            startCalender.set(Calendar.MONTH,month)
            startCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            selectedFromYear = year
            selectedFromMonth = month
            selectedFromDay = dayOfMonth
            TimePickerDialog(requireContext(),startTimePicker,startCalender.get(Calendar.HOUR),startCalender.get(Calendar.MINUTE),false).show()
        }
        DatePickerDialog(requireContext(),startDatePicker,startCalender.get(Calendar.YEAR),startCalender.get(Calendar.MONTH),
            startCalender.get(Calendar.DAY_OF_MONTH)
        ).show()

    }
    private fun setSelectedEndDate(){

        endTimePicker= TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            endCalendar.set(Calendar.HOUR,hourOfDay)
            endCalendar.set(Calendar.MINUTE,minute)
            selectedToHour = hourOfDay
            selectedToMinutes = minute
            setDateText(endCalendar,endDate)
        }

       endDatePicker=  DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
           endCalendar.set(Calendar.YEAR,year)
           endCalendar.set(Calendar.MONTH,month)
           endCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            selectedToYear = year
            selectedToMonth = month
            selectedToDay = dayOfMonth
            TimePickerDialog(requireContext(),endTimePicker,  endCalendar.get(Calendar.HOUR),  endCalendar.get(Calendar.MINUTE),false).show()
        }
        DatePickerDialog(requireContext(),endDatePicker,  endCalendar.get(Calendar.YEAR),  endCalendar.get(Calendar.MONTH),
            endCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()

    }
    private fun createAlertId(start:String, end: String, type: String): Int {
        val input = start+end+type
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4)
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

    override fun onResume() {
        super.onResume()
        alertFactory = AlertViewModelFactory(
            ConcreteRepo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            ),

            )
        alertViewModel = ViewModelProvider(requireActivity(), alertFactory)[AlertViewModel::class.java]
        binding.fabAlert.setOnClickListener {
            showAlertDialoge()
        }

        alertViewModel.getAlerts()
        alertViewModel._alertList.observe(viewLifecycleOwner){
            alertadapter = AlertAdapter(it,this,requireContext())

            binding.rvAlert.apply {
                adapter =alertadapter
                layoutManager = alertManger
            }
        }
    }

    override fun onAlertDeleteListener(alert: AlertPojo) {
        alertViewModel.deleteAlert(alert)
        cancleAlarm(alert.id)
    }


    private fun getLocationFromPrefrence():LatLng{
        val shared = activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
      val lat = shared?.getFloat("lat", 0.0f)?.toDouble()
        val long = shared?.getFloat("long",0.0f)?.toLong()?.toDouble()
        return LatLng(lat!!,long!!)

    }
    private fun requestOverAppPermission() {
        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),20)
    }



    private fun cancleAlarm(id:Int) {

        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, WeatherReciever::class.java)
        for (i in 0..numberOfDays) {
            pendingIntent = getBroadcast(context, id+i, intent, 0)
            alarmManager.cancel(pendingIntent)
        }
    }


    private fun deleteCompletedNotification(alert: AlertPojo, i: Int,deleteTime:Long) {
        val deleteIntent  = Intent(requireActivity(), DeleteNotificationReciever::class.java)
        deleteIntent.putExtra("alert",alert.id+i)
        val deletePending = getBroadcast(requireContext(), alert.id+i, deleteIntent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deleteTime, deletePending)
    }

}
