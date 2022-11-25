package ru.allin.emo

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import ru.allin.emo.Adapter.NumberAdapter


class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: NumberAdapter
    private lateinit var viewPager: ViewPager2
    var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        adapter = NumberAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = adapter
        val sleepCat = findViewById<ImageView>(R.id.sleep_cat)
        sleepCat.setOnClickListener {
            clickCount++
            if(clickCount == 5)
            {
                clickCount = 0
                SoundHelper.meowSound(this)
            }
        }
    }
}