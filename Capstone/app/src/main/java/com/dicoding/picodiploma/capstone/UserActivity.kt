package com.dicoding.picodiploma.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.dicoding.picodiploma.capstone.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var userBinding : ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        actionBar?.title = resources.getString(R.string.liber)



    }
}