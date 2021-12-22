package com.dicoding.picodiploma.capstone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.capstone.databinding.ActivityUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

class UserActivity : AppCompatActivity() {
    private lateinit var userBinding : ActivityUserBinding
    private lateinit var userDB : DatabaseReference
    private var clickedValue: Boolean = false

    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCES_NAME = "liber_preferences"
    val KEY_USER = "key_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        toolbar()
        viewPager()
    }

    private fun toolbar(){
        val myToolbar = userBinding.toolbar
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }
        setSupportActionBar(myToolbar)
    }

    private fun getUser() : String? = sharedPreferences.getString(KEY_USER, null)

    private fun viewPager(){

        userDB = FirebaseDatabase.getInstance().getReference("User")

        userDB.child(getUser().toString()).get().addOnSuccessListener {
            if (it.exists()){
                val usernameData = it.child("id").value.toString()
                val nameData = it.child("name").value.toString()

                userBinding.idUser.text = usernameData
                userBinding.nameUser.text = nameData
            }
        }.addOnFailureListener {
            Toast.makeText(this, "User doesn't exist!", Toast.LENGTH_SHORT).show()
        }

        val tabs = userBinding.tab
        val viewPager = userBinding.viewPager
        val detailBundle = Bundle()
        val intent = intent.getStringExtra(getUser().toString())

        detailBundle.putString(getUser().toString(), intent)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, detailBundle)

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab,position ->
            when (position) {
                0 -> {
                    tab.text = null
                    tab.setIcon(R.drawable.calendar_white)
                }
                1 -> {
                    tab.text = null
                    tab.setIcon(R.drawable.megaphone_white)
                }
            }
        }.attach()

    }

    override fun onBackPressed() {
        if (clickedValue) {

            this.finishAffinity()
            exitProcess(0)
            return
        }
        this.clickedValue = true
        val exitText = resources.getText(R.string.exit)
        Toast.makeText(this, exitText, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ clickedValue = false }, 2000)
    }

    private fun clearUser(){
        val user : SharedPreferences.Editor = sharedPreferences.edit()

        user.clear()
        user.apply()
    }

    private fun logOut(){
        val intent = Intent(this, LoginActivity::class.java)

        clearUser()
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_log_out -> {
                logOut()

                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}