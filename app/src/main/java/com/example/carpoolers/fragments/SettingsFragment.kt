package com.example.carpoolers.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.carpoolers.R
import com.example.carpoolers.Singleton

class SettingsFragment : Fragment() {

    lateinit var checkBox: CheckBox
    lateinit var seekBar: SeekBar
    lateinit var tvDistance: TextView
    var isNightModeOn: Boolean = false

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

        (activity as AppCompatActivity).supportActionBar?.hide()

        var v: View? = view

        if (v != null) {
            checkBox = v.findViewById(R.id.checkBox2)
            seekBar = v.findViewById(R.id.seekBar)
            tvDistance = v.findViewById(R.id.tvDistance)
        }

        tvDistance.text = getString(R.string.swipe_distance, seekBar.progress)
        seekBar.max = 100
        //seekBar.incrementProgressBy(0.1)


        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                checkBox.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                checkBox.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    if (seekBar.progress > 0) {
                        val value = progress / 5.0f
                        tvDistance.text = String.format("%.1f km", value)
                    } else if (seekBar.progress == 0) {
                        tvDistance.text =
                            "Max distance is 0.0 km.\nGood luck getting matches"
                    }
                }

            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    Singleton.swipeDistance = seekBar.progress / 5
                    Toast.makeText(
                        context, "Swipe range was changed to " +
                                "${seekBar.progress / 5.0f} km", Toast.LENGTH_LONG
                    ).show()
                }
            }


        })


        checkBox.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }


        }
    }


}