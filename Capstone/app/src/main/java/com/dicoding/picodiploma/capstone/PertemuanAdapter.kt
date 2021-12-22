package com.dicoding.picodiploma.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PertemuanAdapter(private val pertemuanList : ArrayList<PertemuanData>) : RecyclerView.Adapter<PertemuanAdapter.PertemuanViewHolder>() {
    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCES_NAME = "liber_preferences"
    val KEY_PERTEMUAN = "key_pertemuan"

    inner class PertemuanViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val pertemuan : TextView = itemView.findViewById(R.id.pertemuanId)
        val waktu : TextView = itemView.findViewById(R.id.tanggalId)
        val materi : TextView = itemView.findViewById(R.id.materiId)
        val status : TextView = itemView.findViewById(R.id.status_value)

        val context = itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertemuanViewHolder {
        return PertemuanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_pertemuan, parent, false))
    }

    override fun onBindViewHolder(holder: PertemuanViewHolder, position: Int) {
        val item = pertemuanList[position]

        holder.materi.text = item.materi
        holder.pertemuan.text = item.nama
        holder.status.text = item.status
        holder.waktu.text = item.tanggal

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, KegiatanBelajarActivity::class.java)
            sharedPreferences = holder.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            savePertemuan(item.nama.toString())

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pertemuanList.size

    private fun savePertemuan(pertemuanValue : String) {
        val pertemuan : SharedPreferences.Editor = sharedPreferences.edit()

        pertemuan.putString(KEY_PERTEMUAN, pertemuanValue)
        pertemuan.apply()
    }
}