package com.example.allinemmo.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.ChooseEmmo
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.EmotionListActivity
import com.example.allinemmo.HomeActivity
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.example.allinemmo.R
import com.squareup.picasso.Picasso

class EmmoRecyclerViewAdapter : RecyclerView.Adapter<EmmoRecyclerViewAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emmotion?> = ArrayList()
    class EmmoHolder(item:View) : RecyclerView.ViewHolder(item)
    {
        val img = item.findViewById<ImageView>(R.id.oneEmmo)
        val dayCount = item.findViewById<TextView>(R.id.countDay)
        fun bind(emmo:Emmotion?)
        {
            if(emmo == null)
            {
                return
            }

            if(emmo.imageId == 0)
                dayCount.text = emmo.day.toString()

            Picasso.get().load(ImageToDrawableConverter.FromImageIdToDrawable(emmo.imageId)).fit().centerCrop()
                .into(img)
        }
    }

    fun setItems(marks: ArrayList<Emmotion?>) {
        clearItems()
        this.emmotions.addAll(marks)
    }

    fun clearItems() {
        emmotions.clear()
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
            val emmo = emmotions[position]
            if(emmo != null)
            {
                if(emmo.emmotionId == 0)
                {
                    val intent =  Intent(it.context, ChooseEmmo::class.java)
                    intent.putExtra("emmo", emmo)
                    it.context.startActivity(intent)
                }
                else
                {
                    val intent =  Intent(it.context, EmotionListActivity::class.java)
                    intent.putExtra("date", emmo.date)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}