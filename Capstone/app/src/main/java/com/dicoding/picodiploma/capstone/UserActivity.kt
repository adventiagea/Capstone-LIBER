package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.annotation.StringRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.capstone.databinding.ActivityUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserActivity : AppCompatActivity() {
    private lateinit var userBinding : ActivityUserBinding
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.drawable.calendar,
            R.drawable.megaphone
        )
    }
    //private var menu : Menu ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        val detailBundle = Bundle()
        val tabAdapter = SectionsPagerAdapter(this,detailBundle)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = tabAdapter

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab)
        TabLayoutMediator(tabs, viewPager) { tab,position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }

        /*
        userBinding.apply {
            Glide.with(this@UserActivity)
                .load(R.drawable.person)
                .into(image)
        }

         */

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.notification -> {
                val intent = Intent(this, LoginActivity::class.java)

                startActivity(intent)
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}