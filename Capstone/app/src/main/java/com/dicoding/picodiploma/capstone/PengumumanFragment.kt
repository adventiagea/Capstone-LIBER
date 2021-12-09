package com.dicoding.picodiploma.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.picodiploma.capstone.databinding.FragmentPengumumanBinding
import com.google.firebase.storage.StorageReference

class PengumumanFragment : Fragment(R.layout.fragment_pengumuman) {
    private var pengumumanBinding: FragmentPengumumanBinding ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pengumumanBinding = FragmentPengumumanBinding.bind(view)
    }

}