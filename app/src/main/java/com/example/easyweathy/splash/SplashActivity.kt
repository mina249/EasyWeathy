package com.example.easyweathy.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import com.example.easyweathy.MainActivity
import com.example.easyweathy.R
import com.example.easyweathy.databinding.ActivitySplashBinding
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val shared =this.getSharedPreferences("appPrefrence", Context.MODE_PRIVATE)

        binding.splashCard.animate().translationX(1400f).setDuration(1000).startDelay = 3000
        Handler().postDelayed({
           var isFirtTime =  shared.getBoolean("first",true)
            if(isFirtTime){
                startDialogConfirmation()
                shared.edit().apply {
                    putBoolean("first",false)
                    apply()
                }
            }else{
                startActivity(Intent(this,MainActivity::class.java))
            }

        }, 4000)
        val lang = shared?.getString("Language", "en")!!

            setLocale(lang)


        val actionBar = supportActionBar
        actionBar?.setTitle(getString(R.string.app_name))

    }


    fun startDialogConfirmation() {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.starting_dialoge)
        val window: Window? = dialog.getWindow()
        window?.setBackgroundDrawableResource(R.color.transparent);
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        dialog.findViewById<Button>(R.id.btn_submit_dialogue).setOnClickListener {
            var locationGroup = dialog.findViewById<RadioGroup>(R.id.rg_location_dialogue)
            var selectedId = locationGroup.checkedRadioButtonId
            if (selectedId == R.id.rb_map_dialogue) {
                this?.getSharedPreferences("appPrefrence", MODE_PRIVATE)?.edit()?.apply {
                    putString("location", "Map")
                    apply()
                }

            } else {

                this?.getSharedPreferences("appPrefrence", MODE_PRIVATE)?.edit()?.apply {
                    putString("location", "GPS")
                    apply()

                }
            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }

    }
    /*fun setLocale(lang:String){
        var locale = Locale(lang)
        Locale.setDefault(locale)
        var configuration = Configuration()
        configuration.setLocale(locale)
        this?.resources?.updateConfiguration(configuration,this?.resources?.displayMetrics)
       this.getSharedPreferences(
            "appPrefrence",
            AppCompatActivity.MODE_PRIVATE
        )?.edit()?.apply {
            putString("Language", lang)
            apply()
        }

    }*/

    fun setLocale(lang: String,) {
        val localeNew = Locale(lang)
        Locale.setDefault(localeNew)
        val res: Resources = this.getResources()
        val newConfig = Configuration(res.getConfiguration())
        newConfig.locale = localeNew
        newConfig.setLayoutDirection(localeNew)
        res.updateConfiguration(newConfig, res.getDisplayMetrics())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newConfig.setLocale(localeNew)
           this.createConfigurationContext(newConfig)
            this.getSharedPreferences(
                "appPrefrence",
                AppCompatActivity.MODE_PRIVATE
            )?.edit()?.apply {
                putString("Language", lang)
                apply()
            }
        }
    }
}
