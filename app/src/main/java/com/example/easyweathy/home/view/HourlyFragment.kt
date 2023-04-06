package com.example.easyweathy.home.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentHourlyBinding
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import com.example.easyweathy.utilities.APIState
import com.example.easyweathy.utilities.LocationByGps
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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
    var location:String = ""

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

       weatherFactory = WeatherViewModelFactory(
           ConcreteRepo.getInstance(
               ConcreteRemoteSource,
               ConcreteLocalSource.getInstance(requireContext())
           ),
           requireContext()
       )

       weatherViewModel =
           ViewModelProvider(requireActivity(), weatherFactory).get(WeatherViewModel::class.java)
       var shared = context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
       var lang = shared?.getString("Language", "en")!!
       var units = shared?.getString("Units", "")!!
     //  if(NetWorkChecker.getConnectivity(requireContext())!!) {
           if (location == "MapDone") {
               weatherViewModel.getLocationByMap(units, lang)
           } else if (location == "GPS") {
               var gps = LocationByGps(requireContext())
               gps.getLastLocation()
               gps.location.observe(context as LifecycleOwner) {
                   weatherViewModel.getLocationByGPS(it.first, it.second, units, lang)
               }
           }
      /* }else{
           var shared = context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
           var latitude = shared?.getFloat("lat",0.0f)?.toDouble()
           var logitude = shared?.getFloat("long" , 0.0f)?.toDouble()
           weatherViewModel.getCashedHome(latitude!!,logitude!!)
       }*/
       lifecycleScope.launch {
           weatherViewModel.response.collectLatest {result->

                  when(result){
                      is APIState.Sucess->{
                          weatherResponse = result.weatherResponse

                          hourlyAdapter = HourlyAdapter(weatherResponse.hourly!!)
                          binding.rvHourly.adapter = hourlyAdapter
                          binding.rvHourly.layoutManager = hourlyManger
                      }

                      else -> {}
                  }
                  }

           }

       }
   }


