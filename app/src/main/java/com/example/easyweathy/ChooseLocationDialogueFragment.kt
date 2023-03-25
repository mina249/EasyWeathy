package com.example.easyweathy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.example.easyweathy.databinding.FragmentChooseLocationDialogueBinding

lateinit var binding: FragmentChooseLocationDialogueBinding

class ChooseLocationDialogueFragment : DialogFragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseLocationDialogueBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmitDialogue.setOnClickListener(){
            val selectedId = binding.rgLocationDialogue.checkedRadioButtonId
            var radio = view.findViewById<RadioButton>(selectedId)
            if(radio.text.equals("Map")){
                Navigation.findNavController(view).navigate(R.id.dialoge_to_map)
            }else{
                Navigation.findNavController(view).navigate(R.id.dialoge_to_mapfragment)
            }

        }
    }
}