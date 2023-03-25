package com.example.easyweathy.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.easyweathy.databinding.FragmentDailyBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource


class DailyFragment() : Fragment() {
    lateinit var binding: FragmentDailyBinding
    var dailyAdapter: DailyAdapter?=null
    var dailyManger:LayoutManager?=null
    lateinit var weatherResponse: WeatherResponse
    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        weatherFactory = WeatherViewModelFactory(ConcreteRepo.getInstance(ConcreteRemoteSource),
            requireContext()
        )
        weatherViewModel = ViewModelProvider(this,weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getLocationByGPS()
        weatherViewModel.response.observe(viewLifecycleOwner) {
            weatherResponse = it
            dailyAdapter = DailyAdapter(weatherResponse)
            dailyManger = LinearLayoutManager(requireContext())
            binding.rvDaily.apply {
                adapter = dailyAdapter
                layoutManager = dailyManger

            }

        }
    }

}