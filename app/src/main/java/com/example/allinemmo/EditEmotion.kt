package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.OneItemsClasses.Emmotion
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

        val emmo = intent.extras?.get("emmo") as Emmotion

        text.setSelection(0)
        text.setText(emmo.text)
        day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emmo.date)
        dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emmo.date)
            .uppercase(Locale.getDefault())
        img.setImageResource(emmo.imageId)
        val a = 1
    }
}