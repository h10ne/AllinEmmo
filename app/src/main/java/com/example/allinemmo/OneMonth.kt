package com.example.allinemmo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allinemmo.Adapter.EmmoRecyclerViewAdapter
import com.example.allinemmo.DataBase.DBHelper
import com.example.allinemmo.OneItemsClasses.Emmotion
import java.util.*
import kotlin.collections.ArrayList

const val ARG_OBJECT = "yearAndMonth"
const val DAYS = "daysInMonth"

class NumberFragment : Fragment() {

    private lateinit var yearAndMonth:List<String>
    private var countDays = 0
    private lateinit var year:TextView
    private lateinit var month:TextView
    private lateinit var emmo:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_one_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            year = view.findViewById<TextView>(R.id.yearTV)
            month = view.findViewById<TextView>(R.id.monthTV)
            emmo = view.findViewById<RecyclerView>(R.id.emmoRV)
            yearAndMonth = getString(ARG_OBJECT).toString().split(" ")
            countDays = getInt(DAYS)

            year.text = yearAndMonth[0]
            month.text = yearAndMonth[1]
        }
    }

    override fun onResume() {
        emmo.layoutManager =
            GridLayoutManager(requireContext(), /*if (countDays > 7) 7 else countDays*/ 7)
        val adapter = EmmoRecyclerViewAdapter()
        adapter.setItems(getItemsForAdapter(countDays, yearAndMonth[0].toInt(), yearAndMonth[2].toInt(), yearAndMonth[3].toInt()))

        emmo.adapter = adapter
        super.onResume()
    }

    private fun getItemsForAdapter(count: Int, year: Int, month: Int, firstDayOfWeek: Int): ArrayList<Emmotion?>
    {
        val list = ArrayList<Emmotion?>()
        for (i in 1 until firstDayOfWeek)
        {
            list.add(null)
        }
        val list2 = getEmmotions(count, year, month)
        list.addAll(list2)
        return list
    }

    private fun getEmmotions(count: Int, year: Int, month: Int): ArrayList<Emmotion> {
        val list = ArrayList<Emmotion>()

        val helper = DBHelper(requireContext(), null)
        val emmoList = helper.getEmmoByYearAndMonth(year, month).toSet()

        for (i in 1..count) {
            val emmotion = emmoList.find {
                it.day == i
            }

            list.add(
                emmotion ?: Emmotion(0, 0, "", Date(year - 1900, month - 1, i), i)
            )
        }
        return list
    }

}