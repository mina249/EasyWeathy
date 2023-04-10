package com.example.easyweathy.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
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
import com.example.easyweathy.network.NetWorkChecker
import com.example.easyweathy.utilities.Utility
import java.util.Locale


class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    var shared: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater, container, false)
        shared = activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.settings)
        onMapSelect()
        onGpsSelect()
        onMeterSelect()
        onMileSelect()
        onArabicSelect()
        onEnglishSelect()
        onCelesisSelect()
        onFerhenhiteSelect()
        onKilvenSelect()
        getRadioButtonChecked()
        onDisAbleNotificationSelect()
        onEnableNotificationSelect()

       if (!NetWorkChecker.getConnectivity(requireContext())!!){
                isRadioButtonChecked(false)
        }else{
            isRadioButtonChecked(true)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lottieSetting.repeatCount = Int.MAX_VALUE


    }

    private fun onMapSelect() {
        binding.rbMap.setOnClickListener() {
            if(NetWorkChecker.getConnectivity(requireContext())!!) {
                requireActivity().getSharedPreferences(
                    "appPrefrence",
                    AppCompatActivity.MODE_PRIVATE
                )
                    ?.edit()?.apply {
                        putString("location", "Map")
                        apply()
                        Navigation.findNavController(requireView()).navigate(R.id.setting_to_home)
                    }
            }else{
                Utility.noInternetDialog(requireContext())
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun onGpsSelect() {
        if (!binding.rbGps.isChecked)
            binding.rbGps.setOnClickListener() {
                requireActivity().getSharedPreferences(
                    "appPrefrence",
                    AppCompatActivity.MODE_PRIVATE
                )?.edit()?.apply {
                    putString("location", "GPS")
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
                    putString("Units", "metric")
                    apply()
                }
            binding.rbCelesius.isChecked = true
        }
    }

    fun onMileSelect() {
        binding.rbMileHour.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units", "imperial")
                apply()
            }
            binding.rbFerhenhite.isChecked = true
        }

    }

    fun onCelesisSelect() {
        binding.rbCelesius.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units", "metric")
                apply()
            }
            binding.rbMeterSec.isChecked = true
        }
    }

    fun onKilvenSelect() {
        binding.rbKelvin.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units", "standard")
                apply()
            }
        }
    }

    fun onFerhenhiteSelect() {
        binding.rbFerhenhite.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Units", "imperial")
                apply()

            }
            binding.rbMileHour.isChecked = true
        }

    }


    fun onArabicSelect() {
        binding.rbArabic.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Language", "ar")
                apply()
                setLocale("ar")
                activity?.recreate()
            }



        }
    }

    fun onEnglishSelect() {
        binding.rbEnglish.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Language", "en")
                apply()
                setLocale("en")
                activity?.recreate()
            }

        }

    }

   /* fun setLocale(lang: String) {
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var configuration = Configuration()
        configuration.setLocale(locale)
        context?.resources?.updateConfiguration(configuration, context?.resources?.displayMetrics)

    }*/
   fun setLocale(lng: String,) {
       val localeNew = Locale(lng)
       Locale.setDefault(localeNew)
       val res: Resources = requireContext().getResources()
       val newConfig = Configuration(res.getConfiguration())
       newConfig.locale = localeNew
       newConfig.setLayoutDirection(localeNew)
       res.updateConfiguration(newConfig, res.getDisplayMetrics())
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
           newConfig.setLocale(localeNew)
        requireContext().createConfigurationContext(newConfig)
       }
   }


    fun getRadioButtonChecked() {
        shared = activity?.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)
        var units = shared?.getString("Units", "")
        var lang = shared?.getString("Language", "")
        if (units == "metric") {
            binding.rbCelesius.isChecked = true
            binding.rbMeterSec.isChecked = true
        } else if (units == "standard") {
            binding.rbKelvin.isChecked = true

        } else if (units == "imperial") {
            binding.rbFerhenhite.isChecked = true
            binding.rbMileHour.isChecked = true
        } else {
        }

        if (lang == "en") {
            binding.rbEnglish.isChecked = true
        } else if (lang == "ar") {
            binding.rbArabic.isChecked = true
        } else {

        }

    }

    fun onEnableNotificationSelect() {
        binding.rbEnable.setOnClickListener() {


            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("isAlert", "yes")
                apply()
            }


        }

    }
    fun onDisAbleNotificationSelect() {
        binding.rbDisable.setOnClickListener() {
            requireActivity().getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("isAlert", "no")
                apply()
            }


        }

    }

    fun isRadioButtonChecked(flag:Boolean){
        if(!flag){
           binding.rbEnable.isEnabled = false
           binding.rbDisable.isEnabled = false
           binding.rbMeterSec.isEnabled = false
           binding.rbMileHour.isEnabled = false
           binding.rbFerhenhite.isEnabled = false
           binding.rbCelesius.isEnabled = false
            binding.rbKelvin.isEnabled = false
            binding.rbEnglish.isEnabled = false
            binding.rbArabic.isEnabled = false
            binding.rbGps.isEnabled = false
            binding.rbMap.isEnabled = false
            Utility.noInternetDialog(requireContext())

        }else{
            binding.rbEnable.isEnabled = true
            binding.rbDisable.isEnabled = true
            binding.rbMeterSec.isEnabled = true
            binding.rbMileHour.isEnabled = true
            binding.rbFerhenhite.isEnabled = true
            binding.rbCelesius.isEnabled = true
            binding.rbKelvin.isEnabled = true
            binding.rbEnglish.isEnabled = true
            binding.rbArabic.isEnabled = true
            binding.rbGps.isEnabled = true
            binding.rbMap.isEnabled = true
        }

    }

}







