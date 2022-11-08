package com.example.allinemmo.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.allinemmo.ARG_OBJECT
import com.example.allinemmo.DAYS
import com.example.allinemmo.NumberFragment
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
        val daysInMonth = yearMonthObject.lengthOfMonth() //28
        val displayName = localDate.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        val monthName = localDate.month.value

        val cal = Calendar.getInstance()
        cal.time = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
        cal.set(Calendar.DAY_OF_MONTH, 1)
        var firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK).toInt()
        firstDayOfWeek--
        if(firstDayOfWeek == 0)
            firstDayOfWeek = 7

        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, "${localDate.year} ${displayName.uppercase(Locale.getDefault())} $monthName $firstDayOfWeek")
            putInt(DAYS, if(position == 0) current.dayOfMonth else daysInMonth)
        }
        return fragment
    }

}