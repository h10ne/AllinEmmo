package com.example.allinemmo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
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

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoomout)

        emmo1 = findViewById(R.id.emmo_angry_circle)
        emmo2 = findViewById(R.id.emmo_haha_circle)
        emmo3 = findViewById(R.id.emmo_happy_circle)
        emmo4 = findViewById(R.id.emmo_horny_circle)
        emmo5 = findViewById(R.id.emmo_love_circle)
        emmo6 = findViewById(R.id.emmo_normal_circle)
        emmo7 = findViewById(R.id.emmo_tears_circle)
        emmo8 = findViewById(R.id.emmo_wow_sad_circle)

        emmo1.startAnimation(zoomOut)
        emmo2.startAnimation(zoomOut)
        emmo3.startAnimation(zoomOut)
        emmo4.startAnimation(zoomOut)
        emmo5.startAnimation(zoomOut)
        emmo6.startAnimation(zoomOut)
        emmo7.startAnimation(zoomOut)
        emmo8.startAnimation(zoomOut)

        Handler().postDelayed({
            emmo1.startAnimation(zoomIn)
            emmo2.startAnimation(zoomIn)
            emmo3.startAnimation(zoomIn)
            emmo4.startAnimation(zoomIn)
            emmo5.startAnimation(zoomIn)
            emmo6.startAnimation(zoomIn)
            emmo7.startAnimation(zoomIn)
            emmo8.startAnimation(zoomIn)
        }, 100)

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

        if(emmo.emmotionId == 0)
        {
            emmo.imageId = imgId
            val intent =  Intent(it.context, EditEmotion::class.java)
            intent.putExtra("emmo", emmo)
            it.context.startActivity(intent)
        }
        else
        {
            val data = Intent()
            data.putExtra("imgId", imgId)

            setResult(Activity.RESULT_OK, data)
        }
        finish()
    }
}