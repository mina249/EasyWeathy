package com.example.easyweathy.favourite.view.favourite_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentFavHourlyBinding
import com.example.easyweathy.databinding.FragmentHourlyBinding
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavDetailsViewModelFactory
import com.example.easyweathy.favourite.view.favourite_details.view_model.FavouriteDetailsViewModel
import com.example.easyweathy.home.view.HourlyAdapter
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource
import com.example.easyweathy.network.NetWorkChecker
import java.text.DecimalFormat

class FavHourly : Fragment() {
    lateinit var binding: FragmentFavHourlyBinding
    lateinit var hourlyAdapter: HourlyFavDetailsAdapter
    lateinit var hourlyManger: RecyclerView.LayoutManager
    lateinit var weatherResponse: WeatherResponse
    lateinit var detailsFactory: FavDetailsViewModelFactory
    lateinit var detailsViewModel: FavouriteDetailsViewModel
    lateinit var details:DetailsFavourite
    var latitude = 0.0
    var longitude = 0.0

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
            binding = FragmentFavHourlyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       detailsFactory = FavDetailsViewModelFactory(
            ConcreteRepo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())))
        detailsViewModel =  ViewModelProvider(requireActivity(), detailsFactory).get(FavouriteDetailsViewModel::class.java)
        if (NetWorkChecker.getConnectivity(requireContext())==true){
            detailsViewModel.getWeatherDetailsFromAPI(DetailsFavourite.latitude, DetailsFavourite.longitude)
        }else{
            var formatlatitude = DecimalFormat("##.####").format(DetailsFavourite.latitude)
            DetailsFavourite.latitude = formatlatitude.toDouble()
            var formatlongitude = DecimalFormat("##.####").format(DetailsFavourite.longitude)
            DetailsFavourite.longitude = formatlongitude.toDouble()

            detailsViewModel.getWeatherDetailsFromRoom(
                DetailsFavourite.latitude,
                DetailsFavourite.longitude
            )
        }
        detailsViewModel.responseDetails.observe(viewLifecycleOwner){
            weatherResponse = it
            Log.i("jesus",it.hourly?.size.toString())
            hourlyAdapter = HourlyFavDetailsAdapter(weatherResponse.hourly!!)
           hourlyManger = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            binding.rvHourlyFav.apply {
                adapter = hourlyAdapter
                layoutManager = hourlyManger
            }
        }
    }
}

