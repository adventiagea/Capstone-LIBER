package com.dicoding.picodiploma.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.picodiploma.capstone.databinding.FragmentJadwalPelajaranBinding
import com.dicoding.picodiploma.capstone.databinding.FragmentPengumumanBinding

class JadwalPelajaranFragment : Fragment() {
    private var pelajaranBinding: FragmentJadwalPelajaranBinding?= null
    private val binding get() = pelajaranBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pelajaranBinding = FragmentJadwalPelajaranBinding.inflate(inflater, container, false)

        return binding.root
    }


}