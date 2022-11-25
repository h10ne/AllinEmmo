package com.example.allinemmo.CompanionObjects

import com.example.allinemmo.R

/**
 * Ассоциатор эмоций
 */
object ImageToDrawableConverter {
    /**
     * Получить drawable по ид эмоции
     */
    fun fromImageIdToDrawable(imageId: Int): Int {
        when (imageId) {
            1 -> return R.drawable.emmo_happy
            2 -> return R.drawable.emmo_confused
            3 -> return R.drawable.emmo_perfect
            4 -> return R.drawable.emmo_angry
            5 -> return R.drawable.emmo_normal
            6 -> return R.drawable.emmo_sad
            7 -> return R.drawable.emmo_excited
            8 -> return R.drawable.emmo_tired
        }
        return R.drawable.empty_circle
    }

    /**
     * Получить ид эмоции по ее drawable
     */
    fun fromDrawableToImageId(drawable: Int): Int {
        when (drawable) {
            R.drawable.emmo_happy -> return 1
            R.drawable.emmo_confused -> return 2
            R.drawable.emmo_perfect -> return 3
            R.drawable.emmo_angry -> return 4
            R.drawable.emmo_normal -> return 5
            R.drawable.emmo_sad -> return 6
            R.drawable.emmo_excited -> return 7
            R.drawable.emmo_tired -> return 8
        }
        return 0
    }

    /**
     * Получить название эмоции по ее ид
     */
    fun getEmmoNameById(imageId: Int): String {
        when (imageId) {
            1 -> return "Все отлично!"
            2 -> return "Растерян"
            3 -> return "Чертовски хорош B-)"
            4 -> return "Злюся!"
            5 -> return "Пойдет..."
            6 -> return "Грущу(("
            7 -> return "Взволнован"
            8 -> return "Устяль..."
        }
        return "???"
    }
}