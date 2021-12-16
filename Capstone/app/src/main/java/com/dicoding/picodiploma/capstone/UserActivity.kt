package com.dicoding.picodiploma.capstone

import android.app.Activity
import android.content.Intent
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
    private lateinit var user : String
    private lateinit var userDB : DatabaseReference
    private var clickedValue: Boolean = false

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        userDB = FirebaseDatabase.getInstance().getReference("User")
        user = intent.getStringExtra("user").toString()

        userDB.child(user).get().addOnSuccessListener {
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
        val intent = intent.getStringExtra(EXTRA_USER)

        detailBundle.putString(EXTRA_USER, intent)

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

        supportActionBar?.elevation = 0f

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }

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