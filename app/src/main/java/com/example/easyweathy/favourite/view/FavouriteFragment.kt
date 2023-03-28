package com.example.easyweathy.favourite.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentFavouriteBinding
import com.example.easyweathy.favourite.OnCardFavClickListener
import com.example.easyweathy.favourite.OnFavouriteDeleteListener
import com.example.easyweathy.favourite.view_model.FavouriteViewModel
import com.example.easyweathy.favourite.view_model.FavouriteViewModelFactory
import com.example.easyweathy.home.view.HomeFragmentDirections
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource

/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouriteFragment : Fragment(),OnFavouriteDeleteListener ,OnCardFavClickListener{
     lateinit var binding: FragmentFavouriteBinding
     lateinit var favViewModel:FavouriteViewModel
     lateinit var favFactory:FavouriteViewModelFactory
    var favAdapter:FavouriteAdapter?=null
     var favManger:LayoutManager?=null
    lateinit var weatherResponse: WeatherResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddToFavourite.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.navigate_from_fav_to_map)


            }
        favFactory = FavouriteViewModelFactory(
            ConcreteRepo.getInstance(ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())))
        favViewModel = ViewModelProvider(requireActivity(), favFactory).get(FavouriteViewModel::class.java)
        favViewModel.getAllFavWeather()
        favViewModel._favouriteWeatherList.observe(viewLifecycleOwner){
            Log.i("fav",it.toString())
            favAdapter = FavouriteAdapter(it,this,requireContext(),this)
            favManger = LinearLayoutManager(requireContext())
            binding.rvFavourite.apply {
                adapter = favAdapter
                layoutManager = favManger
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun deleteWeatherFromFavourite(weatherResponse: WeatherResponse) {

        favViewModel.deleteFromFavourite(weatherResponse)
    }

    override fun showFavouriteDetails(lat: Double, long: Double) {
        val action = FavouriteFragmentDirections.navigateFromFavToDetails(lat.toFloat(),long.toFloat())
        Navigation.findNavController(requireView()).navigate(action)
    }


}