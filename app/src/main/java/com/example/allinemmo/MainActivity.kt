package com.example.allinemmo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.allinemmo.DataBase.DBHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val multiline = findViewById<TextView>(R.id.multilineSplash)
        multiline.text = getRandomTint()
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
        Createdatabase()
    }

    private fun Createdatabase() {
        val helper = DBHelper(this, null)
        val date = Date()
        helper.addEmmotion(1, "Все супер", date)
        val emmo = helper.getEmmoById(1);
    }

    private fun getRandomTint(): String? {
        val rnds = (1..34).random()
        val packageName = packageName
        val resId = resources.getIdentifier("SplashTint$rnds", "string", packageName)
        return getString(resId)
    }
}