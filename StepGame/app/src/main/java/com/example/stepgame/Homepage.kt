package com.example.stepgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_homepage.*
import java.util.*


class Homepage : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val name = intent.getStringExtra(EXTRA_User)
        if (name != null) {
            val context: Context = this
            var dbs = DataBaseHandler(context)
            var user = User(
                name.toString(),
                20,
                0,
                0,
                80,
                76,
                0f,
                1,
                0,
                0,
                0,
                0
            )
            dbs.insertData(user)
            dbs.close()
        }
        supportActionBar?.hide()
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        val fragmentAdapter = PagerAdapter(supportFragmentManager, name.toString())
        viewPager.adapter = fragmentAdapter
        viewPager.offscreenPageLimit
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()
    }

    private fun setupTabIcons(){
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_homepage)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_missions)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_achievm)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_settings)
    }

    override fun onBackPressed() {
    }


}