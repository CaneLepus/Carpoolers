package com.example.carpoolers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carpoolers.TabLayoutAdapter.ViewPagerAdapter
import com.example.carpoolers.fragments.*
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "")
        adapter.addFragment(SwipeFragmentJava(), "")
        adapter.addFragment(MatchesFragment(), "")
        adapter.addFragment(ProfilePageFragment(), "")
        adapter.addFragment(SettingsFragment(), "")
        viewPager.adapter = adapter
        viewPager.setPagingEnabled(true)
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_action_name)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_people_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_chat_24)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_person_24)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_baseline_settings_24)
    }
}