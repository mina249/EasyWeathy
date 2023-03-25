package com.example.easyweathy.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.easyweathy.databinding.FragmentHourlyBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource


/**
 * A simple [Fragment] subclass.
 * Use the [HourlyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HourlyFragment : Fragment() {
    lateinit var binding: FragmentHourlyBinding
    lateinit var hourlyAdapter: HourlyAdapter
     lateinit var hourlyManger:LayoutManager
    lateinit var weatherResponse: WeatherResponse
    lateinit var weatherFactory: WeatherViewModelFactory
     lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHourlyBinding.inflate(inflater,container,false)
        hourlyManger = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        return binding.root
    }


   override fun onResume() {
        super.onResume()
        weatherFactory = WeatherViewModelFactory(ConcreteRepo.getInstance(ConcreteRemoteSource),requireContext()
        )
        weatherViewModel = ViewModelProvider(this,weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getLocationByGPS()
        weatherViewModel.response.observe(viewLifecycleOwner) {
            weatherResponse = it
            Log.i("position",it.hourly.toString())
            hourlyAdapter = HourlyAdapter(it.hourly)
            binding.rvHourly.adapter = hourlyAdapter
            binding.rvHourly.layoutManager = hourlyManger
        }

    }



}