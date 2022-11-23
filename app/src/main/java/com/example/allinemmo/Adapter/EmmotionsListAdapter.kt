package com.example.allinemmo.Adapter

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.EditEmotion
import com.example.allinemmo.EmotionListActivity
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.example.allinemmo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EmmotionsListAdapter(val emotionListActivity: EmotionListActivity) :
    RecyclerView.Adapter<EmmotionsListAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emmotion> = ArrayList()

    class EmmoHolder(item: View, val emotionListActivity: EmotionListActivity) :
        RecyclerView.ViewHolder(item) {
        val img = item.findViewById<ImageView>(R.id.person_photo)
        val day = item.findViewById<TextView>(R.id.day_card)
        val dayweek = item.findViewById<TextView>(R.id.dayweek_card)
        val text = item.findViewById<TextView>(R.id.emmo_text_card)
        val bin = item.findViewById<ImageButton>(R.id.bin_emmo)
        val emmoName = item.findViewById<TextView>(R.id.emmo_name)
        val emmo_img = item.findViewById<ImageView>(R.id.emmo_image)

        fun bind(emmo: Emmotion, emmotionsListAdapter: EmmotionsListAdapter) {
            text.text = emmo.text
            emmoName.text = ImageToDrawableConverter.GetEmmoNameById(emmo.imageId)
            day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emmo.date)
            dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emmo.date)
                .uppercase(Locale.getDefault())

            if(emmo.imageSource != "")
            {
                emmo_img.visibility = View.VISIBLE
                val imgFile = File(emmo.imageSource)
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                emmo_img.setImageBitmap(myBitmap)
            }

            bin.setOnClickListener {
                RemoveEmmo(emmo, emmotionsListAdapter)
            }

            img.setImageResource(ImageToDrawableConverter.FromImageIdToDrawable(emmo.imageId))
        }

        private fun RemoveEmmo(emmo: Emmotion, adapter: EmmotionsListAdapter) {
            val dialog = Dialog(emotionListActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.delete_emmo_record)
            val body = dialog.findViewById(R.id.dialog_text) as TextView
            val yesBtn = dialog.findViewById(R.id.yes_bin) as ImageButton
            val noBtn = dialog.findViewById(R.id.no_cross) as ImageButton
            yesBtn.setOnClickListener {
                val db = DBHelper(emotionListActivity.baseContext, null)
                db.deleteEmmoById(emmo.emmotionId)
                adapter.removeItem(adapterPosition)
                dialog.dismiss()
            }
            noBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun setItems(marks: ArrayList<Emmotion>) {
        clearItems()
        this.emmotions.addAll(marks)
        //notifyDataSetChanged()
    }

    fun clearItems() {
        emmotions.clear()
        //notifyDataSetChanged()
    }

    private fun removeItem(position: Int) {
        this.emmotions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, emmotions.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmmoHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_emmo_info, parent, false)
        return EmmoHolder(view, emotionListActivity)
    }

    override fun onBindViewHolder(holder: EmmoHolder, position: Int) {
        holder.bind(emmotions[position], this)

        holder.itemView.setOnClickListener {
            val emmo = emmotions[position]

            emmo.imageId = ImageToDrawableConverter.FromImageIdToDrawable(emmo.imageId)
            val intent =  Intent(it.context, EditEmotion::class.java)
            intent.putExtra("emmo", emmo)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}