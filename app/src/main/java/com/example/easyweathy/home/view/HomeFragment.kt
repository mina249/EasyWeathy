package com.example.easyweathy.home.view

import android.annotation.SuppressLint

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentHomeBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.google.android.material.tabs.TabLayout
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

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var shared = activity?.getSharedPreferences("appPrefrence",MODE_PRIVATE)
         location = shared?.getString("location","")!!
        if (location.equals("Map")) {
            val action = HomeFragmentDirections.homeToMap("Home")
            Navigation.findNavController(view).navigate(action)
        }
        viewAdapter = ViewPagerAdapter(activity?.supportFragmentManager,lifecycle)
        viewPager = binding.viewPagerHome
        viewPager?.adapter = viewAdapter
        tab = binding.tabLayHome
        tab.addTab(tab.newTab().setText("To Day"))
        tab.addTab(tab.newTab().setText("This Week"))
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
    @SuppressLint("SimpleDateFormat")
    override fun onResume() {
        super.onResume()
        weatherFactory = WeatherViewModelFactory(
            ConcreteRepo.getInstance(ConcreteRemoteSource,ConcreteLocalSource.getInstance(requireContext())),
            requireContext()
        )
        weatherViewModel = ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)
        if (location == "MapDone"){
            weatherViewModel.getLocationByMap()
        }else if (location == "GPS"){
            weatherViewModel.getLocationByGPS()
        }
                        weatherViewModel.response.observe(requireActivity()) {
                        weatherResponse = it
                        binding.tvHomeCountry.text = weatherResponse.timezone
                        var milliSecondDate = weatherResponse.current?.dt
                        Log.i("time",milliSecondDate.toString())
                        var date = Date(milliSecondDate?.times(1000L) ?: 0)
                        val timeZoneDate = SimpleDateFormat("dd  MMM , hh : mm a ")
                        var formatedDate = timeZoneDate.format(date)
                        binding.tvHomeDate.text = formatedDate
                        binding.tvHomeTemp.text = weatherResponse.current?.temp.toString()
                        binding.tvHomeDes.text = weatherResponse.current?.weather?.get(0)?.description
                        binding.tvHomeCloud.text = weatherResponse.current?.clouds.toString()
                        binding.tvHomeHumidity.text = weatherResponse.current?.humidity.toString()
                        binding.tvHomePress.text = weatherResponse.current?.pressure.toString()
                        binding.tvHomeWind.text = weatherResponse.current?.wind_speed.toString()
                        binding.tvHomeVisiblity.text = weatherResponse.current?.visibility.toString()
                        binding.tvHomeUltra.text = weatherResponse.current?.uvi.toString()
                    }


        }
}





