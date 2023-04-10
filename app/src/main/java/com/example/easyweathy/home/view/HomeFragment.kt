package com.example.easyweathy.home.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentHomeBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import com.example.easyweathy.utilities.APIState
import com.example.easyweathy.utilities.LocationByGps
import com.example.easyweathy.utilities.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
   lateinit var binding:FragmentHomeBinding
   lateinit var tab:TabLayout
    lateinit var viewPager:ViewPager2
   lateinit var viewAdapter: ViewPagerAdapter
   lateinit var weatherFactory:WeatherViewModelFactory
   lateinit var weatherViewModel:WeatherViewModel
   lateinit var weatherResponse: WeatherResponse
   lateinit var location:String
   lateinit var units:String
   lateinit var lang:String
   lateinit var dialog:Dialog

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

       val actionBar = activity?.actionBar
       actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
       actionBar?.setDisplayShowHomeEnabled(true)
       actionBar?.setDisplayHomeAsUpEnabled(true)
       actionBar?.setTitle(getString(R.string.home))

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAdapter = ViewPagerAdapter(activity?.supportFragmentManager,lifecycle)
        viewPager = binding.viewPagerHome
        viewPager?.adapter = viewAdapter
        tab = binding.tabLayHome
        tab.addTab(tab.newTab().setText(getString(R.string.today)))
        tab.addTab(tab.newTab().setText(getString(R.string.week)))
        tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!=null)
                    viewPager?.currentItem = tab.position

            }

            override fun onTabUnselected(tab:TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tab.selectTab(tab.getTabAt(position))
            }
        })
    }
    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation", "CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        noInternetDialog()
        weatherFactory = WeatherViewModelFactory(
            ConcreteRepo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            ),
            requireContext()
        )
        weatherViewModel = ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)

        var shared = activity?.getSharedPreferences("appPrefrence", MODE_PRIVATE)
        location = shared?.getString("location", "")!!
        lang = shared?.getString("Language", "en")!!
        units = shared?.getString("Units", "standard")!!
        if (location.equals("Map")) {
            val action = HomeFragmentDirections.homeToMap("Home")
            Navigation.findNavController(requireView()).navigate(action)
        }
            if (location == "MapDone") {
                weatherViewModel.getLocationByMap(units, lang)
            } else if (location == "GPS") {
                var gps = LocationByGps(requireContext())
                gps.getLastLocation()
                gps.location.observe(context as LifecycleOwner) {
                    activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)?.edit()?.apply {
                        putFloat("lat", it.first.toFloat())
                        putFloat("long", it.second.toFloat())
                        apply()
                    weatherViewModel.getLocationByGPS(it.first, it.second, units, lang)
                }
            }
        }


    lifecycleScope.launch {
        weatherViewModel.response.collectLatest { result ->
            when (result) {
                is APIState.Sucess -> {
                    weatherResponse = result.weatherResponse
                    activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)?.edit()?.apply {
                        putString("cashedHome",  Gson().toJson(weatherResponse))
                        apply()
                    }
                    binding.child.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                    binding.child.visibility = View.VISIBLE
                    setHomeData()
                    dialog.dismiss()
                }
                is APIState.Failure->{
                    binding.loading.visibility = View.GONE
                    var cash = Gson().fromJson( shared.getString("cashedHome",""),WeatherResponse::class.java)
                    if(shared.contains("cashedHome")){
                        weatherResponse = cash
                        setHomeData()
                        binding.child.visibility = View.VISIBLE
                        Snackbar.make(requireView(),R.string.last_data,Snackbar.ANIMATION_MODE_SLIDE).show()
                    }else {
                        dialog.show()
                        binding.child.visibility = View.GONE
                    }
                }
                else -> {
                    dialog.dismiss()
                    binding.loading.visibility = View.VISIBLE
                    binding.loading.repeatCount = Int.MAX_VALUE
                    binding.child.visibility = View.GONE


                }
            }
        }
    }
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.home)

    }



    @SuppressLint("SetTextI18n")
    private fun setHomeData(){
        var countryName = Utility.getAdressName(weatherResponse.lat,weatherResponse.lon,requireContext())
        if(countryName.equals("invalid")){
            binding.tvHomeCountry.text = weatherResponse.timezone
        }else{
            binding.tvHomeCountry.text = countryName
        }
        var shared = activity?.getSharedPreferences("appPrefrence", MODE_PRIVATE)

        var milliSecondDate = weatherResponse.current?.dt
        Log.i("time", milliSecondDate.toString())
        var date = Date(milliSecondDate?.times(1000L) ?: 0)
        val timeZoneDate = SimpleDateFormat("dd  MMM , hh : mm a ", Locale.forLanguageTag(shared?.getString("Language","")))
        var formatedDate = timeZoneDate.format(date)
        var img = weatherResponse?.current?.weather?.get(0)?.let { Utility.getImage(it.icon) }
        binding.imgHomeWeather.setImageResource(img!!)
        binding.tvHomeDate.text = formatedDate
        binding.tvHomeTemp.text = weatherResponse.current?.temp.toString()+Utility.getUnits(requireContext())[1]
        Log.i("temp", weatherResponse.current?.temp.toString())
        binding.tvHomeDes.text = weatherResponse.current?.weather?.get(0)?.description
        binding.tvHomeCloud.text = weatherResponse.current?.clouds.toString()+"% "
        binding.tvHomeHumidity.text = weatherResponse.current?.humidity.toString()+"% "
        binding.tvHomeWind.text = weatherResponse.current?.wind_speed.toString() + Utility.getUnits(requireContext())[0]
        if (lang == "en") {
            binding.tvHomePress.text = weatherResponse.current?.pressure.toString() + " hPa"
            binding.tvHomeVisiblity.text = weatherResponse.current?.visibility.toString()+" meters"
        }else{
            binding.tvHomePress.text = weatherResponse.current?.pressure.toString() + "هيكتو "
            binding.tvHomeVisiblity.text = weatherResponse.current?.visibility.toString() + "متر"
        }
        binding.tvHomeUltra.text = weatherResponse.current?.uvi.toString()

    }

    fun noInternetDialog() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.no_internet)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(R.color.transparent);
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        var cancel =   dialog.findViewById<Button>(R.id.no_net_cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}







