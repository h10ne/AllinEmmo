package ru.allin.emo.Helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import com.application.isradeleon.notify.Notify
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.R
import java.util.*


class BootReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        schedulePushNotifications(context)
    }

    private fun schedulePushNotifications(context: Context) {
        if(!Config.UseNotify)
            return
        val helper = DBHelper(context, null);
        if(helper.isEmoExistToday())
            return
        Notify.build(context)
            .setTitle("Уведомление")
            .setContent("Кажется, вы забыли отметить эмоцию за сегодня. Отметь сейчас, а то забудешь!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .enableVibration(Config.NotifyVibro)
            .setColor(R.color.pink)
            .show()
        try {
            if (Config.NotifyPlaySound)
            {
                val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class SchedulerNotifyHelper(val context: Context)
{
    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    private val alarmPendingIntent by lazy {
        val intent = Intent(context, BootReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, 0)
    }


    fun schedulePushNotifications() {
        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= Config.NotifyTime.split(':')[0].toInt()) {
                if((get(Calendar.MINUTE) >= Config.NotifyTime.split(':')[1].toInt()))
                add(Calendar.DAY_OF_MONTH, 1)
            }
            set(Calendar.HOUR_OF_DAY, Config.NotifyTime.split(':')[0].toInt())
            set(Calendar.MINUTE, Config.NotifyTime.split(':')[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent
        )


    }
}