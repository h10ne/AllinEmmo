package ru.allin.emo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.Helpers.Config
import ru.allin.emo.OneItemsClasses.Emotion
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        supportActionBar?.hide()
        val switchSound = findViewById<SwitchCompat>(R.id.playSoundsSwitcher)
        val switchDebug = findViewById<SwitchCompat>(R.id.showDebug)

        switchSound.isChecked = Config.PlaySound
        switchDebug.isChecked = Config.ShowDebug

        switchDebug.setOnCheckedChangeListener { buttonView, isChecked ->
            val db = DBHelper(this, null)
            db.setConfigValue("showDebug", buttonView.isChecked.toString())
            Config.ShowDebug = buttonView.isChecked
            SoundHelper.playClickSound(this)
        }

        switchSound.setOnCheckedChangeListener { buttonView, isChecked ->
            val db = DBHelper(this, null)
            db.setConfigValue("playClickSound", buttonView.isChecked.toString())
            Config.PlaySound = buttonView.isChecked
            SoundHelper.playClickSound(this)
        }
    }

    fun DoImport(view: View) {
        SoundHelper.playClickSound(this)
        checkPermissions()

        intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1)
            return

        val path = data?.data?.path!!


        val publickDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        val pathWithoutSpecial = path.substring(path.indexOf(':') + 1)
        val publicWithoutSpecial = publickDir.substring(0, publickDir.lastIndexOf('/') + 1)
        val truePath = publicWithoutSpecial + pathWithoutSpecial
        val file = File(truePath)

        val fileData = file.readText()

        var fileDatas = fileData.split("\n")
        val i = 0;
        val db = DBHelper(this, null)
        fileDatas.forEach {
            val emoPaths = it.split(';')

            if(emoPaths.size >= 3)
            {
                val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val dateField = formatter.parse(emoPaths[0])

                val emo = Emotion(0, emoPaths[1].toInt(), emoPaths[2], dateField, 0, "")
                db.addEmmotion(emo)
            }
        }

        Toast.makeText(this, "Данные успешно импортированы!", Toast.LENGTH_LONG).show()
    }

    fun DoExport(view: View) {
        SoundHelper.playClickSound(this)
        checkPermissions()
        val sb = java.lang.StringBuilder()
        val db = DBHelper(this, null)
        val emos = db.GetAllEmmotions()

        val sdfdmy = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        emos.forEach {
            sb.append("${sdfdmy.format(it.date)};${it.catEmoId};${it.text};")
            sb.appendLine()
        }
        val data = sb.toString()
        val myDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "allin_emmo"
        )
        if (!myDir.exists()) {
            myDir.mkdir()
        }
        val sdf = SimpleDateFormat("dd-mm-yyyy-hh-mm-ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val fname = "Import-$currentDate.allindata"

        val file = File(myDir, fname)
        try {
            file.createNewFile()
            val writer = FileWriter(file)
            writer.append(data)
            writer.flush()
            writer.close()
            Toast.makeText(this, "Имя файла - ${file.path}", Toast.LENGTH_LONG).show()
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