package ru.allin.emo.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.allin.emo.DataBase.Migrations.Migration_202211242
import ru.allin.emo.OneItemsClasses.Emotion
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Вспомогательный класс для взаимодействия с бд
 */
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        var query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                IMAGE_ID + " TEXT," +
                TEXT + " TEXT, " +
                DATE + " TEXT, " +
                DAY + " INTEGER, " +
                MONTH + " INTEGER, " +
                YEAR + " INTEGER, " +
                IMAGE_SOURCE + " INTEGER " + ")")

        db.execSQL(query)

        query = ("CREATE TABLE " + MIGRATION_TABLE_NAME + " ("
                + MIGRATION_VERSION + " TEXT PRIMARY KEY)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    /**
     * Пересоздает базу
     */
    fun recreate() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    /**
     * Добавить запись об эмоции
     */
    fun addEmmotion(emo: Emotion) {
        val db = this.writableDatabase
        val isInserted = db.insert(TABLE_NAME, null, createValues(emo))
        db.close()
    }

    /**
     * Обновить запись об эмоции
     */
    fun updateEmmotion(emo: Emotion) {

        val db = this.writableDatabase
        db.update(
            TABLE_NAME,
            createValues(emo),
            "$ID_COL = ?",
            arrayOf(emo.emotionId.toString())
        )
        db.close()
    }

    /**
     * Создать значения [ContentValues] для записи в таблицу
     */
    private fun createValues(emo: Emotion): ContentValues {
        val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy", Locale.getDefault())
        val dateStr = formatter.format(emo.date)
        val cal = Calendar.getInstance()
        cal.time = emo.date
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val values = ContentValues()
        values.put(IMAGE_ID, emo.catEmoId)
        values.put(TEXT, emo.text)
        values.put(DATE, dateStr)
        values.put(MONTH, month + 1)
        values.put(YEAR, year)
        values.put(DAY, day)
        values.put(IMAGE_SOURCE, emo.imageSource)
        return values
    }

    /**
     * Удалить эмоцию по ее ид
     */
    fun deleteEmmoById(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString())) > 0
    }

    fun getEmmoById(emmoId: Int): Emotion? {
        val db = this.readableDatabase
        db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $ID_COL = ?", arrayOf(emmoId.toString()))
            .use {
                if (it.moveToFirst()) {
                    val emmo = formatCursor(it)
                    it.close()
                    db.close()
                    return emmo
                }
            }
        return null
    }

    /**
     * Получить информацию о всех существующих эмоциях за год [year] и месяц [month] из базы
     */
    fun getEmmoByYearAndMonth(year: Int, month: Int): ArrayList<Emotion> {
        val list = ArrayList<Emotion>()
        val db = this.readableDatabase
        db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $MONTH = ? AND $YEAR = ? ORDER BY $DAY",
            arrayOf(month.toString(), year.toString())
        )
            .use {
                if (it.moveToFirst()) {
                    list.add(formatCursor(it))
                }
                while (it.moveToNext()) {
                    list.add(formatCursor(it))
                }
                it.close()
            }
        db.close()
        return list
    }

    /**
     * Выполнить миграции
     */
    fun applyMigrations() {
        val write = this.writableDatabase
        Migration_202211242().doMigration(write)
        write.close()
    }

    /**
     * Получить объект [Emotion] из запроса [Cursor]
     */
    private fun formatCursor(it: Cursor): Emotion {
        val emmoId = it.getInt(0)
        val imageId = it.getInt(1)
        val text = it.getString(2)
        val date = it.getString(3)
        val day = it.getInt(4)
        val imgsrc = it.getString(7)
        val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy", Locale.getDefault())
        val dateField = formatter.parse(date)
        return Emotion(emmoId, imageId, text, dateField, day, imgsrc)
    }

    companion object {

        // below is variable for database name
        private val DATABASE_NAME = "allin_demo"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "emmo_table"

        // below is the variable for id column
        val ID_COL = "id"

        // ид картинки (свичем выбираем, хз как блоб сделать)
        val IMAGE_ID = "image_id"

        // Текст для дня
        val TEXT = "text"

        // Дата эмоции
        val DATE = "date_column"

        val MONTH = "month_column"

        val YEAR = "year_column"

        val DAY = "day_column"

        val IMAGE_SOURCE = "img_src"


        // below is the variable for table name
        val MIGRATION_TABLE_NAME = "migration_info"

        val MIGRATION_VERSION = "version"
    }
}