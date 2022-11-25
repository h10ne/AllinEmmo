package com.example.allinemmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.Adapter.EmotionsListAdapter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.OneItemsClasses.Emotion
import java.util.*
import kotlin.collections.ArrayList

class EmotionListActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private val adapter = EmotionsListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion_list)
        supportActionBar?.hide()

        recycler = findViewById(R.id.emmotions_card_rv)

        val bck = findViewById<ImageButton>(R.id.emmolist_back_btn)
        recycler.layoutManager = LinearLayoutManager(baseContext)

        bck.setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val date = intent.extras?.get("date") as Date
        val pos = intent.extras?.get("pos") as Int
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        val marks = getMarks(year, month)
        adapter.setItems(marks)
        recycler.adapter = adapter
        recycler.smoothScrollToPosition(pos)
    }

    /**
     * Получить записи эмоций для указанного года [year] и месяца [month]
     */
    private fun getMarks(year: Int, month: Int): ArrayList<Emotion> {
        val db = DBHelper(this.baseContext, null)
        return db.getEmmoByYearAndMonth(year, month)
    }
}