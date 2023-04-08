package com.example.easyweathy.favourite.view.favourite_details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentDailyBinding
import com.example.easyweathy.databinding.FragmentFavDailyBinding
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavDetailsViewModelFactory
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavouriteDetailsViewModel
import com.example.easyweathy.home.view.DailyAdapter
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import com.example.easyweathy.utilities.APIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class FavDaily : Fragment() {
    lateinit var binding: FragmentFavDailyBinding
    var dailyAdapter: DailyFavouriteAdapter?=null
    var dailyManger: RecyclerView.LayoutManager?=null
    lateinit var weatherResponse: WeatherResponse
    lateinit var detailsFactory: FavDetailsViewModelFactory
    lateinit var detailsViewModel: FavouriteDetailsViewModel
    lateinit var details:DetailsFavourite
    var latitude = 0.0
    var longitude = 0.0
    lateinit var units:String
    lateinit var lang:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        details = DetailsFavourite()
        latitude = DetailsFavourite.latitude
        longitude = DetailsFavourite.longitude

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavDailyBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var shared = context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        lang = shared?.getString("Language", "")!!
        units = shared?.getString("Units", "standard")!!

        detailsFactory = FavDetailsViewModelFactory(
            ConcreteRepo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        detailsViewModel = ViewModelProvider(
            requireActivity(),
            detailsFactory
        ).get(FavouriteDetailsViewModel::class.java)
        if (NetWorkChecker.getConnectivity(requireContext()) == true) {
            detailsViewModel.getWeatherDetailsFromAPI(latitude, longitude, units, lang)
        } else {

            var formatlatitude = DecimalFormat("##.####").format(DetailsFavourite.latitude)
            DetailsFavourite.latitude = formatlatitude.toDouble()
            var formatlongitude = DecimalFormat("##.####").format(DetailsFavourite.longitude)
            DetailsFavourite.longitude = formatlongitude.toDouble()

            detailsViewModel.getWeatherDetailsFromRoom(
                DetailsFavourite.latitude,
                DetailsFavourite.longitude
            )
        }
        lifecycleScope.launch {
            detailsViewModel.responseDetails.collectLatest {result->
                when(result){
                    is APIState.Sucess->{
                        dailyAdapter = DailyFavouriteAdapter(result.weatherResponse,requireContext())
                        dailyManger = LinearLayoutManager(requireContext())
                        binding.rvDailyFav.apply {
                            adapter = dailyAdapter
                            layoutManager = dailyManger
                        }
                    }
                    else -> {}
                }


            }

            }
        }
    }

