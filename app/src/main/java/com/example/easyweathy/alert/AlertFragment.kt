package com.example.easyweathy.alert

import android.annotation.SuppressLint
import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.work.*
import com.airbnb.lottie.LottieAnimationView
import com.example.easyweathy.R
import com.example.easyweathy.databinding.FragmentAlarmBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 * Use the [AlertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlertFragment : Fragment(){
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var binding: FragmentAlarmBinding
    var selectedFromDay = 0
    var selectedFromMonth = 0
    var selectedFromYear = 0
    var selectedFromHour = 0
    var selectedFromMinutes = 0
    var selectedToDay = 0
    var selectedToMonth = 0
    var selectedToYear = 0
    var selectedToHour = 0
    var selectedToMinutes = 0
    lateinit var startDate:TextView
    lateinit var endDate :TextView
    var startCalender = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    lateinit var startDateButton: Button
    lateinit var endDateButton: Button
   var startDatePicker:DatePickerDialog.OnDateSetListener?=null
     var endDatePicker:DatePickerDialog.OnDateSetListener?=null
    lateinit var startTimePicker:TimePickerDialog.OnTimeSetListener
    lateinit var endTimePicker:TimePickerDialog.OnTimeSetListener
     var alertType:String ="Alert"
    var alarmId:Int?=null
    var startMilli:Long=0
    var endMilli:Long=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater,container,false)
        val actionBar = activity?.actionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(getString(R.string.alert))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAlert.setOnClickListener(){
            showAlertDialoge()
        }
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
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),WeatherReciever::class.java)
        intent.putExtra("id",id)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),id,intent,0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,start,pendingIntent)

    }
    private fun showAlertDialoge(){
        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_dialoge)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(R.color.transparent);
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        startDate = dialog.findViewById<TextView>(R.id.tv_start_date)
        endDate = dialog.findViewById<TextView>(R.id.tv_end_date)
        val lotie = dialog.findViewById<LottieAnimationView>(R.id.alarm_lottie)
        lotie?.repeatCount = Int.MAX_VALUE
     startDateButton = dialog.findViewById<Button>(R.id.btn_calendar_from)
     startDateButton?.setOnClickListener (){
        setSelectedStartDate()
     }
        endDateButton =dialog.findViewById<Button>(R.id.btn_calendar_to)
        endDateButton?.setOnClickListener {
          setSelectedEndDate()
        }
        dialog.findViewById<RadioButton>(R.id.rb_alertdialogue_alert).setOnClickListener(){
            alertType = "Alert"
        }
        dialog.findViewById<RadioButton>(R.id.rb_notification_alertdialogue).setOnClickListener(){
            alertType = "Notification"
        }

        var save = dialog.findViewById<Button>(R.id.btn_save_alert_dialogue)
        save.setOnClickListener(){
            var id = createAlertId(startDate.text.toString(),endDate.text.toString(),alertType)
            var alert = AlertPojo(startDate.text.toString(),endDate.text.toString(), alertType,id)
            startMilli = startCalender.timeInMillis
            createNotificationChannel(id)
            setAlarm(startMilli,id)
            dialog.dismiss()

        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun setDateText(c:Calendar, date:TextView){
        date.text = SimpleDateFormat("dd,MMM,yyyy \n hh:mm aa").format(c.time)

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
    fun createAlertId(start:String, end: String, type: String): Int {
        val input = start+end+type
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4)
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }
}
