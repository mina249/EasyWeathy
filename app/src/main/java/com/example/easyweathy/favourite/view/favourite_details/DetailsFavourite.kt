package com.example.easyweathy.favourite.view.favourite_details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FavouriteDetailesBinding
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavDetailsViewModelFactory
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavouriteDetailsViewModel
import com.example.easyweathy.home.view.ViewPagerAdapter
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import com.example.easyweathy.utilities.APIState
import com.example.easyweathy.utilities.Utility
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class DetailsFavourite : Fragment() {
    lateinit var binding:FavouriteDetailesBinding
    lateinit var tab: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var viewAdapter: FavouriteDetailsPagerAdapter
    val args : DetailsFavouriteArgs by navArgs()
    lateinit var detailsViewModel:FavouriteDetailsViewModel
    lateinit var detFactory:FavDetailsViewModelFactory
    lateinit var weatherResponse:WeatherResponse
    lateinit var units:String
    lateinit var lang:String
    companion object {
        var latitude = 0.0
        var longitude = 0.0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude = args.lat.toDouble()
        longitude = args.long.toDouble()

        Log.i("yaso3", "$latitude  $longitude")

        var shared =context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        lang = shared?.getString("Language","en")!!
        units =  shared?.getString("Units","standard")!!

    }




    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAdapter = FavouriteDetailsPagerAdapter(activity?.supportFragmentManager, lifecycle)
        viewPager = binding.viewPagerDetails
        viewPager?.adapter = viewAdapter
        tab = binding.tabLayDetails
        tab.addTab(tab.newTab().setText(getString(R.string.today)))
        tab.addTab(tab.newTab().setText(getString(R.string.week)))
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null)
                    viewPager?.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tab.selectTab(tab.getTabAt(position))
            }
        })
        detFactory = FavDetailsViewModelFactory(
            ConcreteRepo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        detailsViewModel = ViewModelProvider(
            requireActivity(),
            detFactory
        ).get(FavouriteDetailsViewModel::class.java)



        if (NetWorkChecker.getConnectivity(requireContext()) == true) {
            detailsViewModel.getWeatherDetailsFromAPI(latitude, longitude, units, lang)
        } else {
            var formatlatitude = DecimalFormat("##.####").format(latitude)
            latitude = formatlatitude.toDouble()
            var formatlongitude = DecimalFormat("##.####").format(longitude)
            longitude = formatlongitude.toDouble()

            detailsViewModel.getWeatherDetailsFromRoom(latitude, longitude)
        }
        lifecycleScope.launch {
            detailsViewModel.responseDetails.collectLatest { result ->
                when (result) {
                    is APIState.Sucess -> {
                        weatherResponse = result.weatherResponse
                        setHomeData()

                    }
                    else -> {}
                }


            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouriteDetailesBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun setHomeData(){
        var shared =context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        binding.tvHomeCountryDetails.text = Utility.getAdressName(weatherResponse.lat,weatherResponse.lon,requireContext())
        var milliSecondDate = weatherResponse.current?.dt
        Log.i("time", milliSecondDate.toString())
        var date = Date(milliSecondDate?.times(1000L) ?: 0)
        val timeZoneDate = SimpleDateFormat("dd  MMM , hh : mm a ",Locale.forLanguageTag(shared?.getString("Language","")))
        var formatedDate = timeZoneDate.format(date)
        binding.tvHomeDateDetails.text = formatedDate
        binding.tvHomeTempDetails.text = weatherResponse.current?.temp.toString()+Utility.getUnits(requireContext())[1]
        val image = Utility.getImage(weatherResponse?.current?.weather?.get(0)?.icon!!)
        binding.imgHomeWeatherDetails.setImageResource(image)
        binding.tvHomeDesDetails.text = weatherResponse.current?.weather?.get(0)?.description
        binding.tvHomeCloudDetails.text = weatherResponse.current?.clouds.toString()
        binding.tvHomeHumidityDetails.text = weatherResponse.current?.humidity.toString()
        binding.tvHomePressDetails.text = weatherResponse.current?.pressure.toString()
        binding.tvHomeWindDetails.text = weatherResponse.current?.wind_speed.toString()+Utility.getUnits(requireContext())[0]
        binding.tvHomeVisiblityDetails.text = weatherResponse.current?.visibility.toString()
        binding.tvHomeUltraDetails.text = weatherResponse.current?.uvi.toString()

    }

}



