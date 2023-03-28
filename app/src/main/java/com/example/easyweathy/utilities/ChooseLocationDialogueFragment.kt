package com.example.easyweathy.utilities

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.easyweathy.MainActivity
import com.example.easyweathy.databinding.StartingDialogeBinding

@SuppressLint("StaticFieldLeak")
lateinit var binding: StartingDialogeBinding

class ChooseLocationDialogueFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = StartingDialogeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmitDialogue.setOnClickListener() {
            val selectedId = binding.rgLocationDialogue.checkedRadioButtonId
            var radio = view.findViewById<RadioButton>(selectedId)
            if (radio.text.equals("Map")) {
                    activity?.getSharedPreferences("appPrefrence", MODE_PRIVATE)?.edit()?.apply {
                        putString("location", "Map")
                        apply()

                    }
            } else {
                    activity?.getSharedPreferences("appPrefrence", MODE_PRIVATE)?.edit()?.apply {
                        putString("location", "GPS")
                        apply()
                    }
            }
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}