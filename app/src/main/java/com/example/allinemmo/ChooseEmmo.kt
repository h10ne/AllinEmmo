package com.example.allinemmo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class ChooseEmmo : AppCompatActivity() {

    private lateinit var emmo1: ImageButton
    private lateinit var emmo2: ImageButton
    private lateinit var emmo3: ImageButton
    private lateinit var emmo4: ImageButton
    private lateinit var emmo5: ImageButton
    private lateinit var emmo6: ImageButton
    private lateinit var emmo7: ImageButton
    private lateinit var emmo8: ImageButton
    private lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_emmo)
        supportActionBar?.hide()

        emmo1 = findViewById(R.id.emmo_angry_circle)
        emmo2 = findViewById(R.id.emmo_haha_circle)
        emmo3 = findViewById(R.id.emmo_happy_circle)
        emmo4 = findViewById(R.id.emmo_horny_circle)
        emmo5 = findViewById(R.id.emmo_love_circle)
        emmo6 = findViewById(R.id.emmo_normal_circle)
        emmo7 = findViewById(R.id.emmo_tears_circle)
        emmo8 = findViewById(R.id.emmo_wow_sad_circle)
        date = intent.extras?.get("date") as Date

        setOnItemClick()
    }

    private fun setOnItemClick() {
        emmo1.setOnClickListener { handleClick(emmo1) }
        emmo2.setOnClickListener { handleClick(emmo2) }
        emmo3.setOnClickListener { handleClick(emmo3) }
        emmo4.setOnClickListener { handleClick(emmo4) }
        emmo5.setOnClickListener { handleClick(emmo5) }
        emmo6.setOnClickListener { handleClick(emmo6) }
        emmo7.setOnClickListener { handleClick(emmo7) }
        emmo8.setOnClickListener { handleClick(emmo8) }
    }

    private fun handleClick(it: ImageButton) {
        val emmo_id = it.id
        val intent =  Intent(it.context, EditEmotion::class.java)
        intent.putExtra("info", "0 $emmo_id")
        intent.putExtra("date", date)
        it.context.startActivity(intent)
    }
}