package ru.allin.emo.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.allin.emo.ARG_OBJECT
import ru.allin.emo.DAYS
import ru.allin.emo.NumberFragment
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

class NumberAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 100

    override fun createFragment(position: Int): Fragment {
        val fragment = NumberFragment()

        val current = LocalDateTime.now()
        val localDate = current.minusMonths(position.toLong())

        val yearMonthObject = YearMonth.of(localDate.year, localDate.month)
        val daysInMonth = yearMonthObject.lengthOfMonth()
        val displayName = localDate.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        val monthName = localDate.month.value

        val cal = Calendar.getInstance()
        cal.time = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
        cal.set(Calendar.DAY_OF_MONTH, 1)
        var firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        firstDayOfWeek--
        if(firstDayOfWeek == 0)
            firstDayOfWeek = 7

        fragment.arguments = Bundle().apply {
            val stringElements = "${localDate.year} ${displayName.uppercase(Locale.getDefault())} $monthName $firstDayOfWeek"
            putString(ARG_OBJECT, stringElements)
            putInt(DAYS, if(position == 0) current.dayOfMonth else daysInMonth)
        }
        return fragment
    }

}