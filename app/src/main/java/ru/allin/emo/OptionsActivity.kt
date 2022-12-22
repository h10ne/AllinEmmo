package ru.allin.emo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ru.allin.emo.DataBase.DBHelper
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
    }

    fun DoImport(view: View) {
        checkPermissions()

    }

    fun DoExport(view: View) {
        checkPermissions()
        var sb = java.lang.StringBuilder()
        var db = DBHelper(this, null)
        val emos = db.GetAllEmmotions()

        emos.forEach {
            sb.append("${it.date};${it.catEmoId};${it.text};${it.emotionId};")
            sb.appendLine()
        }
        var data = sb.toString()
        val myDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "allin_emmo"
        )
        if (!myDir.exists()) {
            myDir.mkdir()
        }
        val sdf = SimpleDateFormat("dd-mm-yyyy-hh-mm-ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        var fname = "Import-$currentDate.txt"
        var file = File(myDir, fname)
        try {
            file.createNewFile()
            val writer = FileWriter(file)
            writer.append(data)
            writer.flush()
            writer.close()
            Toast.makeText(this, "Имя файла - $fname", Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            val str = ex.toString()
        }
    }

    /**
     * Проверяет разрешения на внутренее хранилище. Если нет, то запрашивает
     */
    private fun checkPermissions() {
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1)
        }
    }
}