package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class EditEmotion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emotion)
        supportActionBar?.hide()

        val date = intent.extras?.get("date") as Date
        val a = 1
    }
}