package com.example.carpoolers.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.carpoolers.R

class SettingsFragment : Fragment() {

    lateinit var checkBox: CheckBox
    var isNightModeOn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var v : View? = view

        if (v != null) {
            checkBox = v.findViewById(R.id.checkBox2)
        }

        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {checkBox.isChecked=true}
            Configuration.UI_MODE_NIGHT_NO -> {checkBox.isChecked=false}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }


        }
    }


}