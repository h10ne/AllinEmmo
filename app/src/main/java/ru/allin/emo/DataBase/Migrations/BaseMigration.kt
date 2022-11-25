package ru.allin.emo.DataBase.Migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.allin.emo.DataBase.DBHelper

abstract class BaseMigration {
    abstract var version: String

    fun doMigration(db: SQLiteDatabase) {
        try {
            db.rawQuery(
                "SELECT * FROM ${DBHelper.MIGRATION_TABLE_NAME} WHERE ${DBHelper.MIGRATION_VERSION} = ?",
                arrayOf(version)
            )
                .use {
                    if (it.moveToFirst()) {
                        return
                    } else {
                        Run(db)
                        val values = ContentValues()
                        values.put(DBHelper.MIGRATION_VERSION, version)
                        db.insert(DBHelper.MIGRATION_TABLE_NAME, null, values)
                    }
                }
        } catch (_: Exception) {
        }
    }

    abstract fun Run(db: SQLiteDatabase)
}