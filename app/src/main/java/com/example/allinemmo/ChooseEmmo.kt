package com.example.allinemmo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.allinemmo.OneItemsClasses.Emotion


class ChooseEmmo : AppCompatActivity() {

    private lateinit var emmo: Emotion

    private var emoButtons = ArrayList<ImageButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_emmo)
        supportActionBar?.hide()

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoomout)

        emoButtons.add(findViewById(R.id.emmo_angry_circle))
        emoButtons.add(findViewById(R.id.emmo_haha_circle))
        emoButtons.add(findViewById(R.id.emmo_happy_circle))
        emoButtons.add(findViewById(R.id.emmo_horny_circle))
        emoButtons.add(findViewById(R.id.emmo_love_circle))
        emoButtons.add(findViewById(R.id.emmo_normal_circle))
        emoButtons.add(findViewById(R.id.emmo_tears_circle))
        emoButtons.add(findViewById(R.id.emmo_wow_sad_circle))

        for (i in 0 until emoButtons.count())
        {
            emoButtons[i].startAnimation(zoomOut)
        }

        Handler().postDelayed({
            for (i in 0 until emoButtons.count())
            {
                emoButtons[i].startAnimation(zoomIn)
            }
        }, 100)

        emmo = intent.extras?.get("emmo") as Emotion

        setOnItemClick()
    }

    private fun setOnItemClick() {
        emoButtons[0].setOnClickListener { handleClick(emoButtons[0], R.drawable.emmo_angry) }
        emoButtons[1].setOnClickListener { handleClick(emoButtons[1], R.drawable.emmo_confused) }
        emoButtons[2].setOnClickListener { handleClick(emoButtons[2], R.drawable.emmo_happy) }
        emoButtons[3].setOnClickListener { handleClick(emoButtons[3], R.drawable.emmo_perfect) }
        emoButtons[4].setOnClickListener { handleClick(emoButtons[4], R.drawable.emmo_tired) }
        emoButtons[5].setOnClickListener { handleClick(emoButtons[5], R.drawable.emmo_normal) }
        emoButtons[6].setOnClickListener { handleClick(emoButtons[6], R.drawable.emmo_sad) }
        emoButtons[7].setOnClickListener { handleClick(emoButtons[7], R.drawable.emmo_excited) }
    }


    private fun handleClick(it: ImageButton, imgId: Int)
    {
        // Если было создание эмоции, открыть окно с редактированием, иначе вернуть ид новой
        if(emmo.emotionId == 0)
        {
            emmo.catEmoId = imgId
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