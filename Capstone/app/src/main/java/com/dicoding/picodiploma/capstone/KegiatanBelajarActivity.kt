package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import com.dicoding.picodiploma.capstone.databinding.ActivityPelajaranBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class KegiatanBelajarActivity : AppCompatActivity() {
    private lateinit var pelajaranBinding: ActivityPelajaranBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var myStorage : StorageReference
    private lateinit var pertemuanValue : String
    private lateinit var pelajaranValue : String
    private val value = 1

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

        myStorage = FirebaseStorage.getInstance().reference.child("ini")

        pelajaranBinding.absenButton.setOnClickListener {
            val tanggalVar = SimpleDateFormat("dd/M/yyyy")
            val jamVar = SimpleDateFormat("hh:mm")
            val currentDate = tanggalVar.format(Date())
            val currentDate2 = jamVar.format(Date())

            pelajaranBinding.tanggalView.text = currentDate
            pelajaranBinding.waktuView.text = currentDate2

        }

        pelajaranBinding.submitButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, value)
        }

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

    private fun submitFile(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == value){
            if (resultCode == RESULT_OK) {
                val myFile = data!!.data
                val myFileName : StorageReference = myStorage.child("pdf"+myFile!!.lastPathSegment)

                myFileName.putFile(myFile).addOnSuccessListener {
                    Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                }
            }
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