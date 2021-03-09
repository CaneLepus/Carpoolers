package com.example.carpoolers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carpoolers.TabLayoutAdapter.ViewPagerAdapter
import com.example.carpoolers.fragments.MainMenuFragment
import com.example.carpoolers.fragments.ProfilePageFragment
import com.example.carpoolers.fragments.SwipeFragmentJava
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        setUpTabs()
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MainMenuFragment(), "")
        adapter.addFragment(SwipeFragmentJava(), "Swipe")
        adapter.addFragment(MainMenuFragment(), "Matches")
        adapter.addFragment(ProfilePageFragment(), "Profile")
        adapter.addFragment(MainMenuFragment(), "Settings")
        viewPager.adapter = adapter
        viewPager.setPagingEnabled(true)
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_action_name)
        //tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
        //tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_settings_24)
    }
}