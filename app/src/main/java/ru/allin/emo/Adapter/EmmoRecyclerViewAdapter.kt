package ru.allin.emo.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.allin.emo.ChooseEmmo
import ru.allin.emo.CompanionObjects.ImageToDrawableConverter
import ru.allin.emo.EmotionListActivity
import ru.allin.emo.OneItemsClasses.Emotion
import ru.allin.emo.R
import ru.allin.emo.SoundHelper
import com.squareup.picasso.Picasso

/**
 * Адаптер для иконок эмоций за месяц
 */
class EmmoRecyclerViewAdapter : RecyclerView.Adapter<EmmoRecyclerViewAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emotion?> = ArrayList()
    class EmmoHolder(item:View) : RecyclerView.ViewHolder(item)
    {
        val catImg = item.findViewById<ImageView>(R.id.oneEmmo)
        val dayCount = item.findViewById<TextView>(R.id.countDay)
        fun bind(emmo:Emotion?)
        {
            if(emmo == null)
            {
                return
            }

            dayCount.text = emmo.day.toString()

            Picasso.get().load(ImageToDrawableConverter.fromImageIdToDrawable(emmo.catEmoId)).fit().centerCrop()
                .into(catImg)
        }
    }

    fun setItems(marks: ArrayList<Emotion?>) {
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
            SoundHelper.playClickSound(it.context)
            val emmo = emmotions[position]
            if(emmo != null)
            {
                if(emmo.emotionId == 0)
                {
                    val intent =  Intent(it.context, ChooseEmmo::class.java)
                    intent.putExtra("emmo", emmo)
                    it.context.startActivity(intent)
                }
                else
                {
                    val intent =  Intent(it.context, EmotionListActivity::class.java)
                    intent.putExtra("date", emmo.date)

                    intent.putExtra("pos", getScrollablePosition(position))
                    it.context.startActivity(intent)
                }
            }
        }
    }

    /**
     * Возвращает позицию, на которую должно прокрутиться окно эмоций за месяц
     */
    private fun getScrollablePosition(position: Int):Int
    {
        var pos = 0
        for (i in 1..position)
        {
            if(emmotions[i]!!.emotionId != 0)
            {
                pos++
            }
        }

        var allEmmoCount = 0
        for (i in 1..position)
        {
            if(emmotions[i]!!.emotionId != 0)
            {
                allEmmoCount++
            }
        }

        if(pos == 1)
        {
            pos = 0
        }
        else if(pos == allEmmoCount)
        {
            pos--
        }
        return pos
    }


    override fun getItemCount(): Int {
        return emmotions.count();
    }
}