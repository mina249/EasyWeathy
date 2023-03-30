package com.example.easyweathy.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.easyweathy.R
import com.example.easyweathy.databinding.FragmentSettingBinding
import com.example.easyweathy.home.view.HomeFragmentDirections
import java.util.Locale


class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater, container, false)
        var shared = activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        var location = shared?.getString("location", "")!!
        if (location == "MapDone") {
            binding.rbMap.isChecked = true

        } else {
            binding.rbGps.isChecked = true
        }

        val actionBar = activity?.actionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(getString(R.string.settings))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lottieSetting.repeatCount = Int.MAX_VALUE
        onMapSelect()
        onGpsSelect()
        onMeterSelect()
        onMileSelect()
        onArabicSelect()
        onEnglishSelect()
        onCelesisSelect()
        onFerhenhiteSelect()
        onKilvenSelect()

    }

    private fun onMapSelect() {
        binding.rbMap.setOnClickListener() {
            requireActivity().getSharedPreferences("appPrefrence", AppCompatActivity.MODE_PRIVATE)
                ?.edit()?.apply {
                    putString("location", "Map")
                    apply()
                    Log.i("eslaaaam", "Milaaaad")
                    Navigation.findNavController(requireView()).navigate(R.id.setting_to_home)
                }
        }
    }
       @SuppressLint("SuspiciousIndentation")
       fun onGpsSelect() {
            if(!binding.rbGps.isChecked)
            binding.rbGps.setOnClickListener() {
                requireActivity().getSharedPreferences(
                    "appPrefrence",
                    AppCompatActivity.MODE_PRIVATE
                )?.edit()?.apply {
                    putString("location","GPS")
                    apply()
                    Navigation.findNavController(requireView()).navigate(R.id.setting_to_home)

                }
            }
        }


        fun onMeterSelect() {
            binding.rbMeterSec.setOnClickListener() {
                requireActivity().getSharedPreferences(
                    "appPrefrence",
                    AppCompatActivity.MODE_PRIVATE
                )
                    ?.edit()?.apply {
                        putString("Units","metric")
                        apply()
                    }
            }
        }

    fun onMileSelect(){
    binding.rbMileHour.setOnClickListener() {
        requireActivity().getSharedPreferences(
            "appPrefrence",
            AppCompatActivity.MODE_PRIVATE
        )?.edit()?.apply {
            putString("Units","imperial")
            apply()

        }
    }

}

    fun onCelesisSelect() {
        binding.rbCelesius.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units","metric")
                apply()
            }
        }
    }

    fun onKilvenSelect(){
        binding.rbKelvin.setOnClickListener(){
    requireActivity().getSharedPreferences(
        "appPrefrence",
        AppCompatActivity.MODE_PRIVATE
    )?.edit()?.apply {
        putString("Units","standard")
        apply()
        }
        }
    }

    fun onFerhenhiteSelect(){
        binding.rbFerhenhite.setOnClickListener(){
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units","imperial")
                apply()

            }
        }

        }


    fun onArabicSelect() {
        binding.rbArabic.setOnClickListener() {


            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Language","ar")
                apply()

            }
            setLocale("ar")

        }
    }

   fun onEnglishSelect(){
       binding.rbEnglish.setOnClickListener(){


        requireActivity().getSharedPreferences(
            "appPrefrence",
            AppCompatActivity.MODE_PRIVATE
        )?.edit()?.apply {
            putString("Language","en")
            apply()
        }
           setLocale("en")

    }

}
    fun setLocale(lang:String){
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var configuration = Configuration()
        configuration.setLocale(locale)
        context?.resources?.updateConfiguration(configuration,context?.resources?.displayMetrics)
        activity?.recreate()
    }

}







