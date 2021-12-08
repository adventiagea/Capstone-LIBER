package com.dicoding.picodiploma.capstone

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.capstone.databinding.FragmentJadwalPelajaranBinding
import com.dicoding.picodiploma.capstone.databinding.FragmentPengumumanBinding
import com.google.firebase.database.*
import com.squareup.okhttp.internal.DiskLruCache

class JadwalPelajaranFragment : Fragment(R.layout.fragment_jadwal_pelajaran) {

    private var pelajaranBinding: FragmentJadwalPelajaranBinding?= null
    private val binding get() = pelajaranBinding!!

    private lateinit var dbref : DatabaseReference
    private lateinit var rvPertemuan : RecyclerView
    private var list = ArrayList<PelajaranData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_jadwal_pelajaran)

        rvPertemuan = findViewById(R.id.jadwal_rv)
        rvPertemuan.layoutManager = LinearLayoutManager(this)
        rvPertemuan.setHasFixedSize(true)

        list = arrayListOf<PelajaranData>()
        getUserData()
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("Mata Kuliah")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(PelajaranData::class.java)
                        list.add(user!!)

                    }
                    rvPertemuan.adapter = PelajaranAdapter(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}