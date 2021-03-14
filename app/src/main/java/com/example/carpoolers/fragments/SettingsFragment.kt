package com.example.carpoolers.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.example.carpoolers.R
import com.example.carpoolers.Singleton

class SettingsFragment : Fragment() {

    lateinit var checkBox: CheckBox
    lateinit var seekBar: SeekBar
    lateinit var tvDistance: TextView
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
            seekBar = v.findViewById(R.id.seekBar)
            tvDistance = v.findViewById(R.id.tvDistance)
        }

        tvDistance.text = getString(R.string.swipe_distance, seekBar.progress)


        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {checkBox.isChecked=true}
            Configuration.UI_MODE_NIGHT_NO -> {checkBox.isChecked=false}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    tvDistance.text = getString(R.string.swipe_distance, seekBar.progress)
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    Singleton.swipeDistance = seekBar.progress
                    Toast.makeText(context, "Swipe range was changed to " +
                            "${seekBar.progress} km", Toast.LENGTH_LONG).show()
                }
            }


        })


        checkBox.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }


        }
    }


}