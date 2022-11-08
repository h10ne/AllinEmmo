package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.Adapter.EmmotionsListAdapter
import com.example.allinemmo.OneItemsClasses.Emmotion

class EmotionListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion_list)
        supportActionBar?.hide()
        val cv = findViewById<RecyclerView>(R.id.emmotions_card_rv)
        cv.layoutManager = LinearLayoutManager(baseContext)
        val adapter = EmmotionsListAdapter()
        val marks = getMarks()
        adapter.setItems(marks)
        cv.adapter = adapter
    }

    private fun getMarks():ArrayList<Emmotion>
    {
        val list = ArrayList<Emmotion>()

        for (i in 1..2) {
            //list.add(Emmotion(i, 0))
        }
        return list
    }
}