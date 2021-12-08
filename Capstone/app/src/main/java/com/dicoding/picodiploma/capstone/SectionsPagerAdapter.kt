package com.dicoding.picodiploma.capstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter (activity:AppCompatActivity, bundle: Bundle) :
    FragmentStateAdapter(activity){
    private val myBundle = bundle

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = JadwalPelajaranFragment()
            1 -> fragment = PengumumanFragment()
        }

        fragment?.arguments = this.myBundle
        return fragment as Fragment
    }
    override fun getItemCount(): Int = 2
}