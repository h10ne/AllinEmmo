package ru.allin.emo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.allin.emo.Adapter.EmmoRecyclerViewAdapter
import ru.allin.emo.DataBase.DBHelper
import ru.allin.emo.OneItemsClasses.Emotion
import java.util.*
import kotlin.collections.ArrayList

const val ARG_OBJECT = "yearAndMonth"
const val DAYS = "daysInMonth"

class NumberFragment : Fragment() {

    private lateinit var yearAndMonth:List<String>
    private var countDaysInMonth = 0
    private lateinit var year:TextView
    private lateinit var month:TextView
    private lateinit var recyclerEmo:RecyclerView
    private lateinit var catSleep:ImageView

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
            recyclerEmo = view.findViewById<RecyclerView>(R.id.emmoRV)
            catSleep = view.findViewById(R.id.sleep_cat)
            yearAndMonth = getString(ARG_OBJECT).toString().split(" ")
            countDaysInMonth = getInt(DAYS)

            year.text = yearAndMonth[0]
            month.text = yearAndMonth[1]

            catSleep.setOnClickListener{
                SoundHelper.meowSound(it.context)
            }
        }
    }

    override fun onResume() {
        recyclerEmo.layoutManager =
            GridLayoutManager(requireContext(),7)
        val adapter = EmmoRecyclerViewAdapter()
        adapter.setItems(getItemsForAdapter(countDaysInMonth, yearAndMonth[0].toInt(), yearAndMonth[2].toInt(), yearAndMonth[3].toInt()))

        recyclerEmo.adapter = adapter
        super.onResume()
    }

    /**
     * Получает список [Emotion] для месяца, в том числе пробелы для выравнивания
     */
    private fun getItemsForAdapter(count: Int, year: Int, month: Int, firstDayOfWeek: Int): ArrayList<Emotion?>
    {
        val list = ArrayList<Emotion?>()
        for (i in 1 until firstDayOfWeek)
        {
            list.add(null)
        }
        val list2 = getEmotions(count, year, month)
        list.addAll(list2)
        return list
    }

    /**
     * Получить спиок [Emotion] для месяца
     */
    private fun getEmotions(count: Int, year: Int, month: Int): ArrayList<Emotion> {
        val list = ArrayList<Emotion>()

        val helper = DBHelper(requireContext(), null)
        val emmoList = helper.getEmmoByYearAndMonth(year, month).toSet()

        for (i in 1..count) {
            val emmotion = emmoList.find {
                it.day == i
            }

            list.add(
                emmotion ?: Emotion(0, 0, "", Date(year - 1900, month - 1, i), i, "")
            )
        }
        return list
    }

}