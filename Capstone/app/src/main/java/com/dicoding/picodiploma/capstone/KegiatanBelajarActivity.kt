package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import com.dicoding.picodiploma.capstone.databinding.ActivityPelajaranBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class KegiatanBelajarActivity : AppCompatActivity() {
    private lateinit var pelajaranBinding: ActivityPelajaranBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var pertemuanValue : String
    private lateinit var pelajaranValue : String

    companion object {
        const val EXTRA_PERTEMUAN = "extra_pertemuan"
        const val EXTRA_NAME = "extra_name" //matkul
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

        pertemuanValue = intent.getStringExtra(EXTRA_PERTEMUAN).toString()
        pelajaranValue = intent.getStringExtra(EXTRA_NAME).toString()

        getData()
    }

    private fun getData(){
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val child1 = dbref.child("0002")
        val child2 = child1.child("Mata Kuliah")

        child2.child(pelajaranValue).get().addOnSuccessListener {
            if (it.exists()){
                val name = it.child("matkul").value.toString()
                val code = it.child("Kelas").value.toString()
                val day = it.child("Hari").value.toString()
                val time = it.child("Jam").value.toString()
                val teacher = it.child("Nama").value.toString()

                pelajaranBinding.apply {
                    pertemuan.text = pertemuanValue
                    namePelajaran.text = name
                    codePelajaran.text = code
                    timePelajaran.text = StringBuilder("$day, $time")
                    pengajar.text = teacher

                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
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