package com.example.allinemmo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.allinemmo.DataBase.DBHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val multiline = findViewById<TextView>(R.id.multilineSplash)
        multiline.text = getRandomTint()
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 0)

        val helper = DBHelper(this, null)
        //helper.recreate()
        //Createdatabase()
    }

    private fun Createdatabase() {
        val helper = DBHelper(this, null)
        helper.recreate()
        var date = GregorianCalendar(2022, 10, 1).time
        /*helper.addEmmotion(1, "Все супер", date)
        date = GregorianCalendar(2022, 10, 2).time
        helper.addEmmotion(2, "Все супер", date)
        date = GregorianCalendar(2022, 10, 3).time
        helper.addEmmotion(3, "Все супер", date)
        date = GregorianCalendar(2022, 10, 4).time
        helper.addEmmotion(4, "Все супер", date)*/
        val emmo = helper.getEmmoById(1)
        val emmoList = helper.getEmmoByYearAndMonth(2022, 10)
    }

    private fun getRandomTint(): String? {
        val rnds = (1..34).random()
        val packageName = packageName
        val resId = resources.getIdentifier("SplashTint$rnds", "string", packageName)
        return getString(resId)
    }
}