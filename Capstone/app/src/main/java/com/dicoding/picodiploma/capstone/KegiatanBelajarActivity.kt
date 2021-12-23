package com.dicoding.picodiploma.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.dicoding.picodiploma.capstone.databinding.ActivityPelajaranBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class KegiatanBelajarActivity : AppCompatActivity() {
    private lateinit var pelajaranBinding: ActivityPelajaranBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var myStorage : StorageReference
    private val value = 1

    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCES_NAME = "liber_preferences"
    val KEY_USER = "key_user"
    val KEY_PELAJARAN = "key_pelajaran"
    val KEY_PERTEMUAN = "key_pertemuan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pelajaranBinding = ActivityPelajaranBinding.inflate(layoutInflater)
        setContentView(pelajaranBinding.root)

        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        myStorage = FirebaseStorage.getInstance().reference
            .child(getPelajaran().toString())
            .child(getPertemuan().toString())
            .child(getUser().toString())

        toolbar()
        submitFile()
        getData()
        absen()
        realtime()
    }

    private fun toolbar(){
        val myToolbar = pelajaranBinding.toolbar
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }
        setSupportActionBar(myToolbar)
    }

    private fun getData(){
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val child1 = dbref.child(getUser().toString())
        val child2 = child1.child("Mata Kuliah")

        child2.child(getPelajaran().toString()).get().addOnSuccessListener {
            if (it.exists()){
                val code = it.child("Kelas").value.toString()
                val day = it.child("Hari").value.toString()
                val time = it.child("Jam").value.toString()
                val teacher = it.child("Nama").value.toString()

                pelajaranBinding.apply {
                    pertemuan.text = getPertemuan().toString()
                    namePelajaran.text = getPelajaran().toString()
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
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val childUser = dbref.child(getUser().toString())
        val childPerkuliahan = childUser.child("Perkuliahan")
        val childMataKuliah = childPerkuliahan.child(getPelajaran().toString())
        val childPertemuan = childMataKuliah.child("Pertemuan")
        val childValue = childPertemuan.child(getPertemuan().toString())
        val childStatus = childValue.child("file")

        val sdfDate = SimpleDateFormat("dd:M:yyyy")
        val sdfHour = SimpleDateFormat("HH:mm")
        val currentDate = sdfDate.format(Date())
        val currentHour = sdfHour.format(Date())

        childValue.get().addOnSuccessListener {
            if (it.exists()){
                val fileStatus = it.child("file").value.toString()
                pelajaranBinding.statusUpload.text = fileStatus

                pelajaranBinding.submitButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "application/pdf"
                    startActivityForResult(intent, value)

                    childStatus.setValue("Uploaded $currentDate $currentHour")
                }
            }

        }


    }

    private fun absen(){
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val childUser = dbref.child(getUser().toString())
        val childPerkuliahan = childUser.child("Perkuliahan")
        val childMataKuliah = childPerkuliahan.child(getPelajaran().toString())
        val childPertemuan = childMataKuliah.child("Pertemuan")
        val childValue = childPertemuan.child(getPertemuan().toString())
        val childStatus = childValue.child("status")

        val sdfDate = SimpleDateFormat("dd:M:yyyy")
        val sdfHour = SimpleDateFormat("HH:mm")
        val currentDate = sdfDate.format(Date())
        val currentHour = sdfHour.format(Date())

        childValue.get().addOnSuccessListener {
            if (it.exists()){
                val hour = it.child("hour").value.toString()
                val open = it.child("open").value.toString()
                val date = it.child("close").value.toString()
                val status = it.child("status").value.toString()

                val absenButton = pelajaranBinding.absenButton
                val absenText = pelajaranBinding.waktuAbsen
                absenText.text = status

                if (currentDate == date){
                    if(currentHour >= open){
                        if (currentHour <= hour){
                            absenButton.text = StringBuilder("Absen")

                            pelajaranBinding.absenButton.isEnabled = true

                            pelajaranBinding.absenButton.setOnClickListener {
                                absenButton.text = StringBuilder("Hadir")

                                Toast.makeText(this, "Absen berhasil!\n${"$currentDate, $currentHour"}", Toast.LENGTH_SHORT).show()

                                childStatus.setValue("$currentDate, $currentHour")
                            }
                        }else{
                            absenButton.text = StringBuilder("Absen")
                            pelajaranBinding.absenButton.isEnabled = false

                            Toast.makeText(this, "Absen sudah ditutup!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        absenButton.text = StringBuilder("Absen")
                        pelajaranBinding.absenButton.isEnabled = false

                        Toast.makeText(this, "Absen belum dibuka!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    absenButton.text = StringBuilder("Absen")
                    pelajaranBinding.absenButton.isEnabled = false

                    Toast.makeText(this, "Absen belum dibuka!", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    private fun realtime(){
        val jam = pelajaranBinding.waktuView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val date = LocalDateTime.now()
            val pattern = "EEEE, dd MMMM yyyy HH:mm:ss"
            val formatDate = DateTimeFormatter.ofPattern(pattern)
            val dayTime = formatDate.format(date)


            jam.text = dayTime
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        dbref = FirebaseDatabase.getInstance().getReference("User")

        val childUser = dbref.child(getUser().toString())
        val childPerkuliahan = childUser.child("Perkuliahan")
        val childMataKuliah = childPerkuliahan.child(getPelajaran().toString())
        val childPertemuan = childMataKuliah.child("Pertemuan")
        val childValue = childPertemuan.child(getPertemuan().toString())
        val childStatus = childValue.child("file")




        val sdfDate = SimpleDateFormat("dd:M:yyyy")
        val sdfHour = SimpleDateFormat("HH:mm")
        val currentDate = sdfDate.format(Date())
        val currentHour = sdfHour.format(Date())



        childStatus.get().addOnSuccessListener {
            if (it.exists()){

                childStatus.setValue("File belum diupload")

                if (requestCode == value){
                    if (resultCode == RESULT_OK) {
                        val myFile = data!!.data
                        val myFileName : StorageReference = myStorage.child("pdf"+myFile!!.lastPathSegment)

                        myFileName.putFile(myFile).addOnSuccessListener {

                            childStatus.setValue("Uploaded $currentDate $currentHour")
                            Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        }


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

    private fun getUser() : String? = sharedPreferences.getString(KEY_USER, null)

    private fun getPelajaran() : String? = sharedPreferences.getString(KEY_PELAJARAN, null)

    private fun getPertemuan() : String? = sharedPreferences.getString(KEY_PERTEMUAN, null)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_log_out-> {
                logOut()

                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}