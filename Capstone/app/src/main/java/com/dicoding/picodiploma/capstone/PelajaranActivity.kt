package com.dicoding.picodiploma.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.capstone.databinding.ActivityPelajaranBinding

class PelajaranActivity : AppCompatActivity() {
    private lateinit var pelajaranBinding: ActivityPelajaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pelajaranBinding = ActivityPelajaranBinding.inflate(layoutInflater)
        setContentView(pelajaranBinding.root)

    }
}