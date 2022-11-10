package com.example.allinemmo.CompanionObjects

import android.graphics.drawable.Drawable
import com.example.allinemmo.R

object ImageToDrawableConverter {
    fun FromImageIdToDrawable(imageId: Int): Int {
        when (imageId) {
            1 -> return R.drawable.emmo_happy
            2 -> return R.drawable.emmo_haha
            3 -> return R.drawable.emmo_horny
            4 -> return R.drawable.emmo_angry
            5 -> return R.drawable.emmo_normal
            6 -> return R.drawable.emmo_tears
            7 -> return R.drawable.emmo_wow_sad
            8 -> return R.drawable.emmo_love
        }
        return R.drawable.empty_circle
    }

    fun FromDrawableToImageId(drawable: Int): Int {
        when (drawable) {
            R.drawable.emmo_happy -> return 1
            R.drawable.emmo_haha -> return 2
            R.drawable.emmo_horny -> return 3
            R.drawable.emmo_angry -> return 4
            R.drawable.emmo_normal -> return 5
            R.drawable.emmo_tears -> return 6
            R.drawable.emmo_wow_sad -> return 7
            R.drawable.emmo_love -> return 8
        }
        return 0
    }

    fun FromDrawableToDrawable(drawable: Int): Int {
        when (drawable) {
            R.drawable.emmo_happy -> return R.drawable.emmo_happy
            R.drawable.emmo_haha -> return R.drawable.emmo_haha
            R.drawable.emmo_horny -> return R.drawable.emmo_horny
            R.drawable.emmo_angry -> return R.drawable.emmo_angry
            R.drawable.emmo_normal -> return R.drawable.emmo_normal
            R.drawable.emmo_tears -> return R.drawable.emmo_tears
            R.drawable.emmo_wow_sad -> return R.drawable.emmo_wow_sad
            R.drawable.emmo_love -> return R.drawable.emmo_love
        }
        return 0
    }
}