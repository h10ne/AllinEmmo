package ru.allin.emo.OneItemsClasses

import java.util.Date

/**
 * Дата класс эмоции
 */
data class Emotion(var emotionId: Int, var catEmoId: Int, var text: String, var date: Date, var day: Int, var imageSource: String):java.io.Serializable