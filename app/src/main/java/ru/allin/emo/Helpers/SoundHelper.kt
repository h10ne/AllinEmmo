package ru.allin.emo

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.content.ContextCompat.getSystemService
import ru.allin.emo.Helpers.Config


object SoundHelper {
    fun playClickSound(context: Context)
    {
        val am = getSystemService(context, AudioManager::class.java)

        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.click_sound1)
        if(am!!.ringerMode == AudioManager.RINGER_MODE_NORMAL && Config.PlaySound)
        {
            mp.start()
        }
    }

    fun meowSound(context: Context)
    {
        val am = getSystemService(context, AudioManager::class.java)
        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.meow)
        if(am!!.ringerMode == AudioManager.RINGER_MODE_NORMAL && Config.PlaySound)
        {
            mp.start()
        }
    }
}