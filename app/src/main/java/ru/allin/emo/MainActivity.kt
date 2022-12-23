package ru.allin.emo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.Helpers.Config
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object
    {
        var wasClicked = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val multiline = findViewById<TextView>(R.id.multilineSplash)
        multiline.text = getRandomTint()
        supportActionBar?.hide()

        val helper = DBHelper(this, null)
        helper.applyMigrations()

        InitConfig(helper)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val  activity = findViewById<ConstraintLayout>(R.id.mainActivityConstraint)
        val intent = Intent(this, HomeActivity::class.java)
        activity.setOnClickListener{
            SoundHelper.playClickSound(this)
            wasClicked = true
            startActivity(intent)
            finish()
        }

        Handler().postDelayed({
            if(!wasClicked)
            {
                startActivity(intent)
                finish()
            }
        }, 5000)
    }

    private fun InitConfig(helper: DBHelper) {
        Config.PlaySound = helper.getConfigValue("playClickSound", "true").toBoolean()
        Config.ShowDebug = helper.getConfigValue("showDebug", "false").toBoolean()
    }

    /**
     * Получает рандомный комментарий из списка для вывода на экран приветствия
     */
    private fun getRandomTint(): String? {
        val rnds = (1..34).random()
        val packageName = packageName
        val resId = resources.getIdentifier("SplashTint$rnds", "string", packageName)
        return getString(resId)
    }
}