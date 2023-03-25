package com.example.easyweathy.map

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
import com.example.easyweathy.R
import com.example.easyweathy.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment(){
        lateinit var map: GoogleMap
        lateinit var location:LatLng
        lateinit var binding:FragmentMapsBinding
   private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
       map = googleMap
       googleMap.clear()
       mapSearch()

       googleMap.setOnMapClickListener {
           googleMap.clear()
           googleMap.addMarker(MarkerOptions().position(it).title("my location"))
           Toast.makeText(requireContext(),"map pressed",Toast.LENGTH_SHORT)
           Log.i("jesus",it.latitude.toString())

       }


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