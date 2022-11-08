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
import com.example.allinemmo.OneItemsClasses.Emmotion
import java.util.*
import kotlin.collections.ArrayList

const val ARG_OBJECT = "yearAndMonth"
const val DAYS = "daysInMonth"

class NumberFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_one_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val year = view.findViewById<TextView>(R.id.yearTV)
            val month = view.findViewById<TextView>(R.id.monthTV)
            val emmo = view.findViewById<RecyclerView>(R.id.emmoRV)
            val yearAndMonth = getString(ARG_OBJECT).toString().split(" ")
            val countDays = getInt(DAYS)
            year.text = yearAndMonth[0]
            month.text = yearAndMonth[1]

            emmo.layoutManager =
                GridLayoutManager(requireContext(), if (countDays > 7) 7 else countDays)
            val adapter = EmmoRecyclerViewAdapter()
            adapter.setItems(GetEmmotions(countDays))

            emmo.adapter = adapter
        }
    }

    private fun GetEmmotions(count: Int): ArrayList<Emmotion> {
        val list = ArrayList<Emmotion>()

        for (i in 1..count) {
            list.add(Emmotion(i, 0, "", Date()))
        }
        return list
    }

}