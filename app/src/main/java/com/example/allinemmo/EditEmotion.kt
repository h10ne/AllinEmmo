package com.example.allinemmo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.allinemmo.CompanionObjects.ImageToDrawableConverter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.OneItemsClasses.Emmotion
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditEmotion : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var day: TextView
    private lateinit var dayweek: TextView
    private lateinit var text: EditText
    private lateinit var photo: ImageButton
    private lateinit var emmoImage: ImageView
    private lateinit var imgBack: CardView
    private lateinit var emmo: Emmotion
    private lateinit var emmoName: TextView

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imgId = result.data?.getIntExtra("imgId", -1)
                emmo.imageId = imgId!!

                img.setImageResource(imgId)
                emmoName.text = ImageToDrawableConverter.GetEmmoNameById(ImageToDrawableConverter.FromDrawableToImageId(imgId!!))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emotion)
        supportActionBar?.hide()

        emmoImage = findViewById(R.id.emmo_image)
        img = findViewById<ImageView>(R.id.person_photo)
        day = findViewById<TextView>(R.id.day_card)
        dayweek = findViewById<TextView>(R.id.dayweek_card)
        text = findViewById<EditText>(R.id.emmo_text_card)
        photo = findViewById(R.id.gallery_btn)
        imgBack = findViewById<CardView>(R.id.cardImgBack)
        val removeImgBtn = findViewById<ImageButton>(R.id.removeImgSrcBtn)

        emmo = intent.extras?.get("emmo") as Emmotion
        val selectImageIntent = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri ->
            emmoImage.setImageURI(uri)
            SaveImage(emmoImage.drawable.toBitmap(), emmo)
        }

        if(emmo.imageSource != "")
        {
            imgBack.visibility = View.VISIBLE
            val imgFile = File(emmo.imageSource)
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            emmoImage.setImageBitmap(myBitmap)
        }

        emmoName = findViewById<TextView>(R.id.emmo_name)
        val clock_btn = findViewById<ImageButton>(R.id.clock_btn)

        clock_btn.setOnClickListener {
            val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())
            text.text.append(currentDate + "\n")
        }

        removeImgBtn.setOnClickListener {
            emmo.imageSource = ""
            emmo.imageId = ImageToDrawableConverter.FromDrawableToImageId(emmo.imageId)
            imgBack.visibility = View.GONE
        }

        emmoImage.setOnClickListener {
            val imgFile = File(emmo.imageSource)
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            StfalconImageViewer.Builder<Bitmap>(this, arrayOf(myBitmap)) { view, image ->
                view.setImageBitmap(image)
            }.show()
        }

        emmoName.text = ImageToDrawableConverter.GetEmmoNameById(ImageToDrawableConverter.FromDrawableToImageId(emmo.imageId))

        text.setSelection(0)
        text.setText(emmo.text)
        day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emmo.date)
        dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emmo.date)
            .uppercase(Locale.getDefault())
        img.setImageResource(emmo.imageId)
        val saveEmmo = findViewById<ImageView>(R.id.apply_btn)

        saveEmmo.setOnClickListener {
            emmo.text = text.text.toString()
            emmo.imageId = ImageToDrawableConverter.FromDrawableToImageId(emmo.imageId)
            saveToDb(emmo)
        }

        photo.setOnClickListener{
            selectImageIntent.launch("image/*")
        }

        img.setOnClickListener {
            val intent =  Intent(it.context, ChooseEmmo::class.java)
            intent.putExtra("emmo", emmo)
            intentLauncher.launch(intent)
        }
    }

    private fun SaveImage(finalBitmap: Bitmap, emmo: Emmotion) {
        checkPermissions()

        val myDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "allin_emmo"
        )

        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        try {
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)

            var file: File
            var fname = ""

            fname = "Image-$n.png"
            file = File(myDir, fname)
            while (file.exists())
            {
                n = generator.nextInt(n)
                fname = "Image-$n.png"
                file = File(myDir, fname)
            }

            try {
                file.createNewFile()
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.flush()
                out.close()
                val db = DBHelper(this, null)
                emmo.imageSource = file.absolutePath
                //db.updateEmmotion(emmo)
                imgBack.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveToDb(emmotion:Emmotion) {
        val helper = DBHelper(baseContext, null)
        if(emmotion.emmotionId == 0)
        {
            helper.addEmmotion(emmotion)
        }
        else
        {
            helper.updateEmmotion(emmotion)

        }
        finish()
    }

    private fun checkPermissions()
    {
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1)
        }
    }
}