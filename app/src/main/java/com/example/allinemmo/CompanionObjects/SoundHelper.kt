package com.example.allinemmo

import android.content.Context
import android.media.MediaPlayer




object SoundHelper {
    fun playClickSound(context: Context)
    {
        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.click_sound1)
        mp.start()
    }
}