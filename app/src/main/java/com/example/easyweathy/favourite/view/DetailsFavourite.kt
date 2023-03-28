package com.example.easyweathy.favourite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.easyweathy.R
import com.example.easyweathy.databinding.FavouriteDetailesBinding
import com.example.easyweathy.home.view.ViewPagerAdapter
import com.example.easyweathy.map.MapsFragmentArgs
import com.google.android.material.tabs.TabLayout


class DetailsFavourite : Fragment() {
    lateinit var binding:FavouriteDetailesBinding
    lateinit var tab: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var viewAdapter: ViewPagerAdapter
    val args : DetailsFavouriteArgs by navArgs()
      var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude = args.lat.toDouble()
        longitude = args.long.toDouble()
        Log.i("directions" , "$latitude  $longitude")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAdapter = ViewPagerAdapter(activity?.supportFragmentManager,lifecycle)
        viewPager = binding.viewPagerDetails
        viewPager?.adapter = viewAdapter
        tab = binding.tabLayDetails
        tab.addTab(tab.newTab().setText("To Day"))
        tab.addTab(tab.newTab().setText("This Week"))
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouriteDetailesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }


}