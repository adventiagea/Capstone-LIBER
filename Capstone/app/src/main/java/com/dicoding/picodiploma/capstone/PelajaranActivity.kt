package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import com.dicoding.picodiploma.capstone.databinding.ActivityPelajaranBinding

class PelajaranActivity : AppCompatActivity() {
    private lateinit var pelajaranBinding: ActivityPelajaranBinding

    companion object {
        var EXTRA_PELAJARAN = "extra_pelajaran"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pelajaranBinding = ActivityPelajaranBinding.inflate(layoutInflater)
        setContentView(pelajaranBinding.root)

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }

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