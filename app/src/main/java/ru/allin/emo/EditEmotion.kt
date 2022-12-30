package ru.allin.emo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import ru.allin.emo.Helpers.ImageToDrawableConverter
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.OneItemsClasses.Emotion
import ru.allin.emo.SoundHelper.playClickSound
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditEmotion : AppCompatActivity(), BottomSheetImagePicker.OnImagesSelectedListener {
    // Картинка-кот с эмоцией
    private lateinit var catEmoImg: ImageView

    // День, за который была эмоция
    private lateinit var day: TextView

    // День недели, за который была эмоция
    private lateinit var dayweek: TextView

    // Текстовая заметка ко дню
    private lateinit var text: EditText

    // Кнопка добавления фотографии из галереи
    private lateinit var photo: ImageButton

    // Прикрепленное изображение
    private lateinit var attachmentImage: ImageView

    // Фон за картинкой
    private lateinit var ImageBackgroundCard: CardView

    // Данные эмоции
    private lateinit var emo: Emotion

    // Название эмоции
    private lateinit var emoName: TextView

    // Кнопка применения изменеий
    private lateinit var saveEmmo: ImageView

    // Кнопка для вставки текущего времени
    private lateinit var clockBtn: ImageButton

    // Кнопка удаления прикрпеленного изображения
    private lateinit var removeImgBtn: ImageButton

    // Действие обновления картинки-кота эмоции
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imgId = result.data?.getIntExtra("imgId", -1)
                emo.catEmoId = imgId!!

                Picasso.get().load(imgId).fit().centerCrop()
                    .into(catEmoImg)
                emoName.text = ImageToDrawableConverter.getEmmoNameById(
                    ImageToDrawableConverter.fromDrawableToImageId(imgId!!)
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emotion)
        supportActionBar?.hide()

        // Инициализация вьюх
        attachmentImage = findViewById(R.id.emmo_image)
        catEmoImg = findViewById(R.id.person_photo)
        day = findViewById(R.id.day_card)
        dayweek = findViewById(R.id.dayweek_card)
        text = findViewById(R.id.emmo_text_card)
        photo = findViewById(R.id.gallery_btn)
        ImageBackgroundCard = findViewById(R.id.cardImgBack)
        emoName = findViewById(R.id.emmo_name)
        saveEmmo = findViewById(R.id.apply_btn)
        clockBtn = findViewById(R.id.clock_btn)
        removeImgBtn = findViewById(R.id.removeImgSrcBtn)

        emo = intent.extras?.get("emmo") as Emotion

        // Если есть прикрепленное изображение, устанавливаем его
        if (emo.imageSource != "") {
            ImageBackgroundCard.visibility = View.VISIBLE
            val imgFile = File(emo.imageSource)

            Picasso.get().load(imgFile).fit().centerCrop()
                .into(attachmentImage)
        }

        // Устанавливаем значения в остальные поля
        emoName.text = ImageToDrawableConverter.getEmmoNameById(
            ImageToDrawableConverter.fromDrawableToImageId(emo.catEmoId)
        )
        text.setSelection(0)
        text.setText(emo.text)

        day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emo.date)
        dayweek.text = SimpleDateFormat("EE", Locale.getDefault()).format(emo.date)
            .uppercase(Locale.getDefault())
        Picasso.get().load(emo.catEmoId).fit().centerCrop()
            .into(catEmoImg)

        initListeners()
    }

    /**
     * Инициализировать слушателей
     */
    private fun initListeners() {
        saveEmmo.setOnClickListener {
            playClickSound(this)
            emo.text = text.text.toString()
            emo.catEmoId = ImageToDrawableConverter.fromDrawableToImageId(emo.catEmoId)
            saveToDb(emo)
        }

        photo.setOnClickListener {
            playClickSound(this)
            BottomSheetImagePicker.Builder("Провидер")
                .cameraButton(ButtonType.Tile)
                .loadingText(R.string.picker_loading_text)
                .galleryButton(ButtonType.Tile)
                .singleSelectTitle(R.string.picker_header_text)
                .columnSize(R.dimen.columnSize)
                .requestTag("single")
                .show(supportFragmentManager)
        }

        catEmoImg.setOnClickListener {
            playClickSound(this)
            val intent = Intent(it.context, ChooseEmmo::class.java)
            intent.putExtra("emmo", emo)
            intentLauncher.launch(intent)
        }


        clockBtn.setOnClickListener {
            playClickSound(this)
            val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())
            text.text.append(currentDate + "\n")
        }

        removeImgBtn.setOnClickListener {
            playClickSound(this)
            emo.imageSource = ""
            emo.catEmoId = ImageToDrawableConverter.fromDrawableToImageId(emo.catEmoId)
            ImageBackgroundCard.visibility = View.GONE
        }

        attachmentImage.setOnClickListener {
            playClickSound(this)
            val imgFile = File(emo.imageSource)
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            StfalconImageViewer.Builder(this, arrayOf(myBitmap)) { view, _ ->
                Picasso.get().load(imgFile).into(view)
            }.show()
        }
    }

    /**
     * Сохраняет изображение в память
     */
    private fun saveImage(finalBitmap: Bitmap, emmo: Emotion) {
        checkPermissions()

        try {
            val file = createUniqFile()
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            emmo.imageSource = file.absolutePath
            ImageBackgroundCard.visibility = View.VISIBLE
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Создает уникальный объект [File]
      */
    private fun createUniqFile(): File {
        val myDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "allin_emmo"
        )

        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)

        var fname = ""

        fname = "Image-$n.png"
        var file = File(myDir, fname)
        while (file.exists()) {
            n = generator.nextInt(n)
            fname = "Image-$n.png"
            file = File(myDir, fname)
        }
        file.createNewFile()
        return file
    }

    /**
     * Сохранить эмоцию в бд и закрыть активити
     */
    private fun saveToDb(emmotion: Emotion) {
        val helper = DBHelper(baseContext, null)
        if (emmotion.emotionId == 0) {
            helper.addEmotion(emmotion)
        } else {
            helper.updateEmotion(emmotion)

        }
        finish()
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

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        attachmentImage.setImageURI(uris[0])
        saveImage(attachmentImage.drawable.toBitmap(), emo)
    }
}