package com.example.easyweathy.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

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
   lateinit var mng:FragmentManager
   lateinit var mngr : FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewAdapter = ViewPagerAdapter(activity?.supportFragmentManager,lifecycle)
        viewPager = binding.viewPagerHome
        viewPager?.adapter = viewAdapter
        tab = binding.tabLayHome

        tab.addTab(tab.newTab().setText("To Day"))
        tab.addTab(tab.newTab().setText("This Week"))



        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
            ConcreteRepo.getInstance(ConcreteRemoteSource),
            requireContext()
        )
        weatherViewModel = ViewModelProvider(this, weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getLocationByGPS()
        weatherViewModel.response.observe(requireActivity()) {
            weatherResponse = it
            binding.tvHomeCountry.text = weatherResponse.timezone
            var milliSecondDate = weatherResponse.current.dt
            var date = Date(milliSecondDate * 1000L)
            val timeZoneDate = SimpleDateFormat("dd  MMM , hh : mm a ")
            val timeZone = SimpleDateFormat("hh : mm a")
            var formateTime = timeZone.format(date)
            var formatedDate = timeZoneDate.format(date)
            binding.tvHomeDate.text = formatedDate //+ " , " + formateTime
            binding.tvHomeTemp.text = weatherResponse.current.temp.toString()
            binding.tvHomeDes.text = weatherResponse.current.weather.get(0).description
            binding.tvHomeCloud.text = weatherResponse.current.clouds.toString()
            binding.tvHomeHumidity.text = weatherResponse.current.humidity.toString()
            binding.tvHomePress.text = weatherResponse.current.pressure.toString()
            binding.tvHomeWind.text = weatherResponse.current.wind_speed.toString()
            binding.tvHomeVisiblity.text = weatherResponse.current.visibility.toString()
            binding.tvHomeUltra.text = weatherResponse.current.uvi.toString()

        }


    }


}