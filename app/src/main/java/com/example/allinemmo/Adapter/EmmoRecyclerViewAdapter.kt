package com.example.allinemmo.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.EmotionListActivity
import com.example.allinemmo.HomeActivity
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.example.allinemmo.R

class EmmoRecyclerViewAdapter : RecyclerView.Adapter<EmmoRecyclerViewAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emmotion> = ArrayList()
    class EmmoHolder(item:View) : RecyclerView.ViewHolder(item)
    {
        val img = item.findViewById<ImageView>(R.id.oneEmmo)
        val dayCount = item.findViewById<TextView>(R.id.countDay)
        fun bind(emmo:Emmotion)
        {
            dayCount.text = emmo.date.day.toString()
            img.setImageResource(R.drawable.empty_circle)
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
                .inflate(R.layout.one_emotion, parent, false)
        return EmmoHolder(view)
    }

    override fun onBindViewHolder(holder: EmmoHolder, position: Int) {
        holder.bind(emmotions[position])

        holder.itemView.setOnClickListener {
            val intent =  Intent(it.context, EmotionListActivity::class.java)
            it.context.startActivity(intent)
            Toast.makeText(it.context, "Клик на кружок", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}