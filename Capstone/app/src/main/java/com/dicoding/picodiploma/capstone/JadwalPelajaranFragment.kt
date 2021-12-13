package com.dicoding.picodiploma.capstone

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.capstone.databinding.FragmentJadwalPelajaranBinding
import com.dicoding.picodiploma.capstone.databinding.FragmentPengumumanBinding
import com.google.firebase.database.*
import com.squareup.okhttp.internal.DiskLruCache

class JadwalPelajaranFragment : Fragment(R.layout.fragment_jadwal_pelajaran) {

    private var pelajaranBinding: FragmentJadwalPelajaranBinding?= null
    private lateinit var dbref : DatabaseReference
    private var list = ArrayList<PelajaranData>()
    private lateinit var userSchedule : String

    companion object{
        const val EXTRA_USER_JADWAL = "extra_user_jadwal"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pelajaranBinding = FragmentJadwalPelajaranBinding.bind(view)

        userSchedule = activity?.intent?.getStringExtra("user").toString()
        Toast.makeText(activity, userSchedule, Toast.LENGTH_SHORT).show()

        val pelajaranRV = pelajaranBinding?.jadwalRv
        pelajaranRV?.layoutManager = LinearLayoutManager(activity)
        pelajaranRV?.setHasFixedSize(true)

        list = arrayListOf()
        getUserData()
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("User")

        val child1 = dbref.child(userSchedule)
        val child2 = child1.child("Mata Kuliah")

        child2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(PelajaranData::class.java)
                        list.add(user!!)

                    }

                    val pelajaranRV = pelajaranBinding?.jadwalRv
                    pelajaranRV?.adapter = PelajaranAdapter(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}