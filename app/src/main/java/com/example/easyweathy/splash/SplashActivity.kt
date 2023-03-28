package com.example.easyweathy.splash

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

import com.example.easyweathy.MainActivity
import com.example.easyweathy.databinding.ActivitySplashBinding
import com.example.easyweathy.utilities.ChooseLocationDialogueFragment


class SplashActivity : AppCompatActivity() {
    lateinit var builder: AlertDialog.Builder
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashCard.animate().translationX(1400f).setDuration(1000).startDelay = 3000
        Handler().postDelayed({
            builder = AlertDialog.Builder(this)
            builder.create()
           var dialog = ChooseLocationDialogueFragment()
           dialog.show(supportFragmentManager,"initial dialogue")
          // startActivity(Intent(this,MainActivity::class.java))
          //finish()
        }, 4000)
    }

}
