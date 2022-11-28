package ru.allin.emo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ru.allin.emo.Adapter.NumberAdapter


class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: NumberAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var sleepCat:ImageView
    var clickCount = 0
    private val lastTouchDownXY = FloatArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        adapter = NumberAdapter(this)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = adapter
        sleepCat = findViewById<ImageView>(R.id.sleep_cat)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                if(state == 1)
                {
                    val cat = viewPager.findViewById<ImageView>(R.id.sleep_cat)
                    cat.visibility = View.INVISIBLE
                }
                else if(state == 0)
                {
                    val cat = viewPager.findViewById<ImageView>(R.id.sleep_cat)
                    cat.visibility = View.VISIBLE
                }

                super.onPageScrollStateChanged(state)
            }
        })
    }
}