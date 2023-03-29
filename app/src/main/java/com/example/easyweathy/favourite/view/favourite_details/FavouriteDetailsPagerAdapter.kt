package com.example.easyweathy.favourite.view.favourite_details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.easyweathy.home.view.DailyFragment
import com.example.easyweathy.home.view.HourlyFragment

class FavouriteDetailsPagerAdapter(fragmentManager: FragmentManager?, lifeCycle: Lifecycle):
    FragmentStateAdapter(fragmentManager!!,lifeCycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0)
                FavHourly()
        else
           FavDaily()
    }
}