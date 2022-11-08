package com.example.allinemmo.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.allinemmo.OneItemsClasses.Emmotion
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                IMAGE_ID + " TEXT," +
                TEXT + " TEXT" +
                DATE + " TEXT" +
                MONTH + " INTEGER" +
                YEAR + " INTEGER" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addEmmotion(imageId: Int, text: String, date: Date) {
        val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy", Locale.getDefault())
        val dateStr = formatter.format(date)
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        val values = ContentValues()
        values.put(IMAGE_ID, imageId)
        values.put(TEXT, text)
        values.put(DATE, dateStr)
        values.put(MONTH, month + 1)
        values.put(YEAR, year)
        this.writableDatabase.insert(DATABASE_NAME, null, values)
    }

    fun updateEmmotion(emmoId: Int, imageId: Int, text: String, date: String) {
        val values = ContentValues()
        values.put(IMAGE_ID, imageId)
        values.put(TEXT, text)
        values.put(DATE, date)
        this.writableDatabase.update(
            DATABASE_NAME,
            values,
            "$ID_COL = ?",
            arrayOf(emmoId.toString())
        )
    }

    // below method is to get
    // all data from our database
    fun getEmmoById(emmoId: Int): Emmotion? {
        this.readableDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + "WHERE $ID_COL = ?", arrayOf(emmoId.toString()))
            .use {
                val emmoId = it.getInt(1)
                val imageId = it.getInt(1)
                val text = it.getString(2)
                val date = it.getString(3)
                val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy", Locale.getDefault())
                val dateField = formatter.parse(date)
                val result = Emmotion(emmoId, imageId, text, dateField)
                return result
            }
        return null
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
        val DATE = "date"

        val MONTH = "month"

        val YEAR = "year"
    }
}