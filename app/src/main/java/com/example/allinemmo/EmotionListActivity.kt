package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.Adapter.EmmotionsListAdapter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.OneItemsClasses.Emmotion
import java.util.*
import kotlin.collections.ArrayList

class EmotionListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion_list)
        supportActionBar?.hide()
        val cv = findViewById<RecyclerView>(R.id.emmotions_card_rv)
        cv.layoutManager = LinearLayoutManager(baseContext)
        val adapter = EmmotionsListAdapter(this)
        val date = intent.extras?.get("date") as Date
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        val marks = getMarks(year, month)
        adapter.setItems(marks)
        cv.adapter = adapter


        val bck = findViewById<ImageButton>(R.id.emmolist_back_btn)

        bck.setOnClickListener {
            finish()
        }
    }

    private fun getMarks(year: Int, month: Int): ArrayList<Emmotion> {
        val db = DBHelper(this.baseContext, null)
        return db.getEmmoByYearAndMonth(year, month)
    }
}