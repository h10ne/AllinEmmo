package com.example.allinemmo.CompanionObjects

import android.graphics.drawable.Drawable
import com.example.allinemmo.R

object ImageToDrawableConverter {
    fun FromImageIdToDrawable(imageId:Int): Int
    {
        when (imageId) {
            1 -> return R.drawable.emmo_happy
            2 -> return R.drawable.emmo_haha
            3 -> return R.drawable.emmo_horny
            4 -> return R.drawable.emmo_angry
            5 -> return R.drawable.emmo_normal
            6 -> return R.drawable.emmo_tears
            7 -> return R.drawable.emmo_wow_sad
        }
        return R.drawable.empty_circle
    }
}