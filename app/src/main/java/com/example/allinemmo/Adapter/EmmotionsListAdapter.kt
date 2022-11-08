package com.example.allinemmo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.example.allinemmo.R

class EmmotionsListAdapter : RecyclerView.Adapter<EmmotionsListAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emmotion> = ArrayList()
    class EmmoHolder(item: View) : RecyclerView.ViewHolder(item)
    {
        val img = item.findViewById<ImageView>(R.id.person_photo)
        val day = item.findViewById<TextView>(R.id.day_card)
        val dayweek = item.findViewById<TextView>(R.id.dayweek_card)
        fun bind(emmo: Emmotion)
        {
            img.setImageResource(R.drawable.emmo_angry)
        }
    }

    fun setItems(marks: ArrayList<Emmotion>) {
        clearItems()
        this.emmotions.addAll(marks)
        notifyDataSetChanged()
    }

    fun clearItems() {
        emmotions.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmmoHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_emmo_info, parent, false)
        return EmmoHolder(view)
    }

    override fun onBindViewHolder(holder: EmmoHolder, position: Int) {
        holder.bind(emmotions[position])

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "Клик на кружок", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}