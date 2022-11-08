package com.example.allinemmo.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.allinemmo.ARG_OBJECT
import com.example.allinemmo.DAYS
import com.example.allinemmo.NumberFragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class NumberAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 100

    override fun createFragment(position: Int): Fragment {
        val fragment = NumberFragment()

        val current = LocalDateTime.now()
        val month = current.minusMonths(position.toLong())

        val yearMonthObject = YearMonth.of(month.year, month.month)
        val daysInMonth = yearMonthObject.lengthOfMonth() //28
        val displayName = month.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())

        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, "${month.year} ${displayName.uppercase(Locale.getDefault())}" )
            putInt(DAYS, if(position == 0) current.dayOfMonth else daysInMonth)
        }
        return fragment
    }

}