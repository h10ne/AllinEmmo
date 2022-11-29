package ru.allin.emo.Adapter

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.allin.emo.Helpers.ImageToDrawableConverter
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.EditEmotion
import ru.allin.emo.EmotionListActivity
import ru.allin.emo.OneItemsClasses.Emotion
import ru.allin.emo.R
import ru.allin.emo.SoundHelper
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер для списка эмоций за месяц
 */
class EmotionsListAdapter(private val emotionListActivity: EmotionListActivity) :
    RecyclerView.Adapter<EmotionsListAdapter.EmmoHolder>() {
    private var emmotions: ArrayList<Emotion> = ArrayList()

    class EmmoHolder(item: View, private val emotionListActivity: EmotionListActivity) :
        RecyclerView.ViewHolder(item) {
        // Картинка-кот с эмоцией
        private val carEmoImg = item.findViewById<ImageView>(R.id.person_photo)

        // День, за который была эмоция
        private val day = item.findViewById<TextView>(R.id.day_card)

        // День недели, за который была эмоция
        private val weekday = item.findViewById<TextView>(R.id.dayweek_card)

        // Текстовая заметка ко дню
        private val text = item.findViewById<TextView>(R.id.emmo_text_card)

        // Кнопка удаления
        private val bin = item.findViewById<ImageButton>(R.id.bin_emmo)

        // Название эмоции
        private val emoName = item.findViewById<TextView>(R.id.emmo_name)

        // Прикрепленное изображение
        private val attachmentImage = item.findViewById<ImageView>(R.id.emmo_image)

        // Кнопка для возвращения назад
        private val imgBack = item.findViewById<CardView>(R.id.cardImgBack)

        fun bind(emo: Emotion, emotionsListAdapter: EmotionsListAdapter) {

            // Установить значения во вью из данных эмоции
            text.text = emo.text
            emoName.text = ImageToDrawableConverter.getEmmoNameById(emo.catEmoId)
            day.text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(emo.date)
            weekday.text = SimpleDateFormat("EE", Locale.getDefault()).format(emo.date)
                .uppercase(Locale.getDefault())
            Picasso.get().load(ImageToDrawableConverter.fromImageIdToDrawable(emo.catEmoId)).fit()
                .centerCrop()
                .into(carEmoImg)

            // Показать остальные элементы, если для них есть значения
            if (emo.text != "") {
                text.visibility = View.VISIBLE
            }

            if (emo.imageSource != "") {
                imgBack.visibility = View.VISIBLE
                val imgFile = File(emo.imageSource)
                Picasso.get().load(imgFile).fit().centerCrop()
                    .into(attachmentImage);
            }

            initListeners(emo, emotionsListAdapter)
        }

        /**
         * Установить слушателей
         */
        private fun initListeners(emmo: Emotion, emmotionsListAdapter: EmotionsListAdapter)
        {
            attachmentImage.setOnClickListener {
                SoundHelper.playClickSound(emotionListActivity)
                val imgFile = File(emmo.imageSource)
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                StfalconImageViewer.Builder(
                    emotionListActivity,
                    arrayOf(myBitmap)
                ) { view, _ ->
                    Picasso.get().load(imgFile).into(view)
                }.show()
            }

            bin.setOnClickListener {
                SoundHelper.playClickSound(emotionListActivity)
                removeEmo(emmo, emmotionsListAdapter)
            }
        }

        /**
         * Вызывать диалог с удалением эмоции
         */
        private fun removeEmo(emmo: Emotion, adapter: EmotionsListAdapter) {
            val dialog = Dialog(emotionListActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.delete_emmo_record)
            val body = dialog.findViewById(R.id.dialog_text) as TextView
            val yesBtn = dialog.findViewById(R.id.yes_bin) as ImageButton
            val noBtn = dialog.findViewById(R.id.no_cross) as ImageButton
            yesBtn.setOnClickListener {
                SoundHelper.playClickSound(emotionListActivity)
                val db = DBHelper(emotionListActivity.baseContext, null)
                db.deleteEmmoById(emmo.emotionId)
                adapter.removeItem(adapterPosition)
                dialog.dismiss()
            }
            noBtn.setOnClickListener {
                SoundHelper.playClickSound(emotionListActivity)
                dialog.dismiss()
            }
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.windowAnimations = R.style.DialogAnimation
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.horizontalMargin = 0f
            lp.verticalMargin = 0f
            dialog.window!!.attributes = lp
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }
    }

    fun setItems(marks: ArrayList<Emotion>) {
        clearItems()
        this.emmotions.addAll(marks)
    }

    private fun clearItems() {
        emmotions.clear()
    }

    private fun removeItem(position: Int) {
        this.emmotions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, emmotions.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmmoHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_emmo_info, parent, false)
        return EmmoHolder(view, emotionListActivity)
    }

    override fun onBindViewHolder(holder: EmmoHolder, position: Int) {
        holder.bind(emmotions[position], this)

        holder.itemView.setOnClickListener {
            SoundHelper.playClickSound(it.context)
            val emmo = emmotions[position]

            emmo.catEmoId = ImageToDrawableConverter.fromImageIdToDrawable(emmo.catEmoId)
            val intent = Intent(it.context, EditEmotion::class.java)
            intent.putExtra("emmo", emmo)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return emmotions.count();
    }
}