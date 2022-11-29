package ru.allin.emo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ru.allin.emo.Adapter.NumberAdapter


class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: NumberAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var sleepCat:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        adapter = NumberAdapter(this)
        viewPager = findViewById<ViewPager2>(R.id.month_pager)
        viewPager.adapter = adapter
        sleepCat = findViewById<ImageView>(R.id.sleep_cat)
        val optionsBtn = findViewById<ImageButton>(R.id.options_btn)

        optionsBtn.setOnClickListener {
            val intent = Intent(it.context, OptionsActivity::class.java)
            startActivity(intent)
        }

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