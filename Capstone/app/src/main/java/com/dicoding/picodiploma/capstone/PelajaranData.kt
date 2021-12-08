package com.dicoding.picodiploma.capstone

data class PelajaranData(
    val matkul: String ?= null,
    val Kelas: String ?= null,
    val Nama: String ?= null,
    val Jam: String ?= null,
    val Hari : String ?= null,
    val Pertemuan : Int ?= null
)
