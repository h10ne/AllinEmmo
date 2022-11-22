package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.OneItemsClasses.Emmotion
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class EditEmotion : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var day: TextView
    private lateinit var dayweek: TextView
    private lateinit var text: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emotion)
        supportActionBar?.hide()

        img = findViewById<ImageView>(R.id.person_photo)
        day = findViewById<TextView>(R.id.day_card)
        dayweek = findViewById<TextView>(R.id.dayweek_card)
        text = findViewById<EditText>(R.id.emmo_text_card)
        val emmoName = findViewById<TextView>(R.id.emmo_name)
        val clock_btn = findViewById<ImageButton>(R.id.clock_btn)

        clock_btn.setOnClickListener {
            val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())
            text.text.append(currentDate + "\n")
        }

        val emmo = intent.extras?.get("emmo") as Emmotion
        emmoName.text = ImageToDrawableConverter.GetEmmoNameById(ImageToDrawableConverter.FromDrawableToImageId(emmo.imageId))

        text.setSelection(0)
        text.setText(emmo.text)
        day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emmo.date)
        dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emmo.date)
            .uppercase(Locale.getDefault())
        img.setImageResource(emmo.imageId)
        val saveEmmo = findViewById<ImageView>(R.id.apply_btn)

        saveEmmo.setOnClickListener {
            saveToDb(emmo.emmotionId, emmo.imageId, text.text.toString(), emmo.date)
        }
    }

    private fun saveToDb(emmotionId:Int, imageId: Int, text: String, day: Date) {
        val helper = DBHelper(baseContext, null)
        if(emmotionId == 0)
        {
            helper.addEmmotion(ImageToDrawableConverter.FromDrawableToImageId(imageId), text, day)
        }
        else
        {
            helper.updateEmmotion(emmotionId, ImageToDrawableConverter.FromDrawableToImageId(imageId), text, day)

        }
        finish()
    }
}