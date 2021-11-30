package com.dicoding.picodiploma.capstone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.picodiploma.capstone.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashBinding : ActivitySplashScreenBinding
    private val splashHandler = Handler(Looper.getMainLooper())
    private val splashDelay : Int = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        splashHandler.postDelayed({
            val splashIntent = Intent(this, LoginActivity::class.java)

            startActivity(splashIntent)
            finish()
        }, splashDelay.toLong())
    }
}