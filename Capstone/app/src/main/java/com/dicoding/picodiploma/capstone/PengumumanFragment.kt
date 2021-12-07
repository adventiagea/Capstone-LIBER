package com.dicoding.picodiploma.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.picodiploma.capstone.databinding.FragmentPengumumanBinding
import com.google.firebase.storage.StorageReference

class PengumumanFragment : Fragment() {
    private var pengumumanBinding: FragmentPengumumanBinding ?= null
    private val binding get() = pengumumanBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pengumumanBinding = FragmentPengumumanBinding.inflate(inflater, container, false)


        return binding.root
    }

}