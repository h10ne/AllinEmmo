package com.example.allinemmo.Adapter

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.EditEmotion
import com.example.allinemmo.EmotionListActivity
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.example.allinemmo.R
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
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
        val imgBack = item.findViewById<CardView>(R.id.cardImgBack)

        fun bind(emmo: Emmotion, emmotionsListAdapter: EmmotionsListAdapter) {
            text.text = emmo.text
            emmoName.text = ImageToDrawableConverter.GetEmmoNameById(emmo.imageId)
            day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emmo.date)
            dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emmo.date)
                .uppercase(Locale.getDefault())

            if (emmo.text != "") {
                text.visibility = View.VISIBLE
            }

            if (emmo.imageSource != "") {
                imgBack.visibility = View.VISIBLE
                val imgFile = File(emmo.imageSource)
                Picasso.get().load(imgFile).fit().centerCrop()
                    .into(emmo_img);
            }

            bin.setOnClickListener {
                RemoveEmmo(emmo, emmotionsListAdapter)
            }

            Picasso.get().load(ImageToDrawableConverter.FromImageIdToDrawable(emmo.imageId)).fit().centerCrop()
                .into(img)

            emmo_img.setOnClickListener {
                val imgFile = File(emmo.imageSource)
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                StfalconImageViewer.Builder(
                    emotionListActivity,
                    arrayOf(myBitmap)
                ) { view, _ ->
                    Picasso.get().load(imgFile).into(view)
                }.show()
            }
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
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.windowAnimations = R.style.DialogAnimation
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.horizontalMargin = 0f
            lp.verticalMargin = 0f
            dialog.window!!.attributes= lp
            dialog.setCanceledOnTouchOutside(true)
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
            val intent = Intent(it.context, EditEmotion::class.java)
            intent.putExtra("emmo", emmo)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}