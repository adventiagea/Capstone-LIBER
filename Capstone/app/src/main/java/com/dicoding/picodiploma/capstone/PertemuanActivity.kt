package com.dicoding.picodiploma.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.capstone.databinding.ActivityPertemuanBinding
import com.google.firebase.database.*

class PertemuanActivity : AppCompatActivity() {
    private lateinit var pertemuanBinding : ActivityPertemuanBinding
    private lateinit var dbref : DatabaseReference
    private var list = ArrayList<PertemuanData>()

    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCES_NAME = "liber_preferences"
    val KEY_USER = "key_user"
    val KEY_PELAJARAN = "key_pelajaran"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pertemuanBinding = ActivityPertemuanBinding.inflate(layoutInflater)
        setContentView(pertemuanBinding.root)

        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        list = arrayListOf()

        toolbar()
        getPertemuanData()
        getJadwalData()
        goHome()
        recyclerView()
    }

    private fun recyclerView(){
        val pertemuanRV = pertemuanBinding.pertemuanRv

        pertemuanRV.layoutManager = LinearLayoutManager(this)
        pertemuanRV.setHasFixedSize(true)
    }

    private fun toolbar(){
        val myToolbar = pertemuanBinding.toolbar
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }
        setSupportActionBar(myToolbar)

    }

    private fun getJadwalData(){
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val child1 = dbref.child(getUser().toString())
        val child2 = child1.child("Mata Kuliah")

        child2.child(getPelajaran().toString()).get().addOnSuccessListener {
            if (it.exists()){
                val name = it.child("matkul").value.toString()
                val code = it.child("Kelas").value.toString()
                val day = it.child("Hari").value.toString()
                val time = it.child("Jam").value.toString()
                val teacher = it.child("Nama").value.toString()

                pertemuanBinding.apply {
                    namePelajaran.text = name
                    codePelajaran.text = code
                    timePelajaran.text = StringBuilder("$day, $time")
                    lecturePelajaran.text = teacher
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPertemuanData() {
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val child1 = dbref.child(getUser().toString())
        val child2 = child1.child("Perkuliahan")
        val child3 = child2.child(getPelajaran().toString())
        val child4 = child3.child("Pertemuan")

        child4.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (mySnapshot in snapshot.children){
                        val mine = mySnapshot.getValue(PertemuanData::class.java)


                        list.add(mine!!)
                    }
                    val pertemuanRV = pertemuanBinding.pertemuanRv
                    pertemuanRV.adapter = PertemuanAdapter(list)
                }

                else {
                    Toast.makeText(this@PertemuanActivity, "data dikata gak ada", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PertemuanActivity, "database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUser() : String? = sharedPreferences.getString(KEY_USER, null)

    private fun getPelajaran() : String? = sharedPreferences.getString(KEY_PELAJARAN, null)

    private fun goHome(){
        pertemuanBinding.title.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun clearUser(){
        val user : SharedPreferences.Editor = sharedPreferences.edit()

        user.clear()
        user.apply()
    }

    private fun logOut(){
        val intent = Intent(this@PertemuanActivity, LoginActivity::class.java)

        clearUser()
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_log_out -> {
                logOut()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}