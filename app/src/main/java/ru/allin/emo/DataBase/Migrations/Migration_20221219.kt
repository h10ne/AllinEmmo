package ru.allin.emo.DataBase.Migrations

import android.database.sqlite.SQLiteDatabase

class Migration_20221219 : BaseMigration() {
    override var version: String = "20221219"

    override fun Run(db: SQLiteDatabase) {
        var sql = "CREATE TABLE CONFIG (" +
                "name TEXT PRIMARY KEY," +
                "value TEXT)"
        db.execSQL(sql)
    }

}