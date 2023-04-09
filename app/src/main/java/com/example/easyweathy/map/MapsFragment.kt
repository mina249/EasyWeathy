package com.example.easyweathy.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavViewModelStoreProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.easyweathy.R
import com.example.easyweathy.database.ConcreteLocalSource
import com.example.easyweathy.databinding.FragmentMapsBinding
import com.example.easyweathy.favourite.view_model.FavouriteViewModel
import com.example.easyweathy.favourite.view_model.FavouriteViewModelFactory
import com.example.easyweathy.home.view.viewmodel.WeatherViewModel
import com.example.easyweathy.home.view.viewmodel.WeatherViewModelFactory
import com.example.easyweathy.model.ConcreteRepo
import com.example.easyweathy.model.WeatherResponse
import com.example.easyweathy.network.ConcreteRemoteSource

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.io.IOException

class MapsFragment : Fragment(){
        lateinit var map: GoogleMap
        lateinit var binding:FragmentMapsBinding
        lateinit var favViewModel: FavouriteViewModel
        lateinit var favFactory:FavouriteViewModelFactory
        var latitude:Double = 0.0
        var longtiude:Double = 0.0
         lateinit var units:String
         lateinit var lang:String

        val args :MapsFragmentArgs by navArgs()
    lateinit var weatherResponse:WeatherResponse
   private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
       map = googleMap
       mapSearch()


       googleMap.setOnMapClickListener {
           googleMap.clear()
           googleMap.addMarker(MarkerOptions().position(it).title("my location"))
           latitude = it.latitude
           longtiude = it.longitude

       }

       var shared =context?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
       lang = shared?.getString("Language","")!!
       units =  shared?.getString("Units","")!!


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.fabDone.setOnClickListener() {

            if(args.home == "Home") {
                activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)?.edit()?.apply {
                    putString("location", "MapDone")
                    apply()

                }
                activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)?.edit()?.apply {
                    putFloat("lat", latitude.toFloat())
                    putFloat("long", longtiude.toFloat())
                    apply()
                    Navigation.findNavController(view).navigate(R.id.map_to_home)

                }
            }else{
                favFactory = FavouriteViewModelFactory(
                    ConcreteRepo.getInstance(ConcreteRemoteSource,ConcreteLocalSource.getInstance(requireContext())))

                favViewModel = ViewModelProvider(requireActivity(), favFactory).get(FavouriteViewModel::class.java)
                CoroutineScope(Dispatchers.Main).launch {
                    favViewModel.insertFavouriteWeather(latitude, longtiude, units, lang)
                    delay(500)
                    Navigation.findNavController(view).navigate(R.id.map_to_favourite)
                }


            }
        }
    }
    fun mapSearch(){

        binding.etSearchMap.setOnEditorActionListener { v, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH||
                    actionId==EditorInfo.IME_ACTION_DONE||
                    event.action == KeyEvent.ACTION_DOWN||
                        event.action == KeyEvent.KEYCODE_ENTER
            ){
                    goToSearchLocation()
                true
            } else {
                binding.etSearchMap.text.clear()
                binding.etSearchMap.hint = "Please Enter Valid Location"
                false
            }

        }
    }

    private fun goToSearchLocation() {
       var serchLocation =    binding.etSearchMap.text.toString()
        var geoCoder = Geocoder(requireContext())
        var adresses = listOf<Address>()
        try {
            adresses = geoCoder.getFromLocationName(serchLocation,1) as List<Address>
        }catch (e:IOException){
            e.printStackTrace()
        }
        if(adresses.isNotEmpty()){
            var adress = adresses[0]
            var latitude = adress.latitude
            var longtuide = adress.longitude
            goToLatLong(latitude,longtuide,17f)

        }
    }

    private fun goToLatLong(latitude: Double, longtiude: Double, fl: Float) {
        var latLang = LatLng(latitude,longtiude)
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang,10f)
        map.animateCamera(cameraUpdate)
        map.addMarker(MarkerOptions().position(latLang))

    }
    fun getLatLang(location:LatLng): LatLng{
        return location
    }


}