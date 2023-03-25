package com.example.easyweathy.home.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager?, lifeCycle:Lifecycle):
    FragmentStateAdapter(fragmentManager!!,lifeCycle) {


    override fun getItemCount(): Int {
      return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0)
            HourlyFragment()
        else
            DailyFragment()

    }
}