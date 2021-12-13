package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
    private lateinit var user : String
    private lateinit var matkulPertemuan : String

    companion object {
        const val EXTRA_MATKUL_PERTEMUAN = "extra_matkul_pertemuan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pertemuanBinding = ActivityPertemuanBinding.inflate(layoutInflater)
        setContentView(pertemuanBinding.root)

        user = intent.getStringExtra("user").toString()

        matkulPertemuan = intent.getStringExtra(EXTRA_MATKUL_PERTEMUAN).toString()

        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show()

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        val iconToolbar = resources.getDrawable(R.drawable.ic_setting)

        myToolbar.inflateMenu(R.menu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myToolbar.overflowIcon = iconToolbar
        }

        val pertemuanRV = pertemuanBinding.pertemuanRv
        pertemuanRV.layoutManager = LinearLayoutManager(this)
        pertemuanRV.setHasFixedSize(true)

        list = arrayListOf()
        getUserData()
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("User")
        //dbref = FirebaseDatabase.getInstance().getReference("Pertemuan")

        val child1 = dbref.child("0002")
        val child2 = child1.child("Perkuliahan")
        val child3 = child2.child(matkulPertemuan)
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