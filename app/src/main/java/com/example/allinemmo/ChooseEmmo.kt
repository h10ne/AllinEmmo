package com.example.allinemmo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.allinemmo.OneItemsClasses.Emmotion


class ChooseEmmo : AppCompatActivity() {

    private lateinit var emmo1: ImageButton
    private lateinit var emmo2: ImageButton
    private lateinit var emmo3: ImageButton
    private lateinit var emmo4: ImageButton
    private lateinit var emmo5: ImageButton
    private lateinit var emmo6: ImageButton
    private lateinit var emmo7: ImageButton
    private lateinit var emmo8: ImageButton
    private lateinit var emmo: Emmotion

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
        emmo = intent.extras?.get("emmo") as Emmotion

        setOnItemClick()
    }

    private fun setOnItemClick() {
        emmo1.setOnClickListener { handleClick(emmo1, R.drawable.emmo_angry) }
        emmo2.setOnClickListener { handleClick(emmo2, R.drawable.emmo_confused) }
        emmo3.setOnClickListener { handleClick(emmo3, R.drawable.emmo_happy) }
        emmo4.setOnClickListener { handleClick(emmo4, R.drawable.emmo_perfect) }
        emmo5.setOnClickListener { handleClick(emmo5, R.drawable.emmo_tired) }
        emmo6.setOnClickListener { handleClick(emmo6, R.drawable.emmo_normal) }
        emmo7.setOnClickListener { handleClick(emmo7, R.drawable.emmo_sad) }
        emmo8.setOnClickListener { handleClick(emmo8, R.drawable.emmo_excited) }
    }

    private fun handleClick(it: ImageButton, imgId: Int) {
        emmo.imageId = imgId

        val intent =  Intent(it.context, EditEmotion::class.java)
        intent.putExtra("emmo", emmo)
        it.context.startActivity(intent)
        finish()
    }
}