package com.dicoding.picodiploma.capstone

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PelajaranAdapter(private val list:ArrayList<PelajaranData>) : RecyclerView.Adapter<PelajaranAdapter.ListViewHolder>(){

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNampel: TextView = itemView.findViewById(R.id.name_pelajaran)
        val tvKodpel: TextView = itemView.findViewById(R.id.code_pelajaran)
        val tvDosen: TextView = itemView.findViewById(R.id.lecture_pelajaran)
        val tvHari: TextView = itemView.findViewById(R.id.day)
        val tvJam: TextView = itemView.findViewById(R.id.hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_pelajaran, parent,false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentitem = list[position]

        holder.tvNampel.text = currentitem.matkul
        holder.tvKodpel.text = currentitem.Kelas
        holder.tvDosen.text = currentitem.Nama
        holder.tvJam.text = currentitem.Jam
        holder.tvHari.text = currentitem.Hari

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, PertemuanActivity::class.java)
            intent.putExtra(PertemuanActivity.EXTRA_MATKUL_PERTEMUAN, currentitem.matkul.toString())

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}