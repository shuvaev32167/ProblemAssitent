package ru.crazypeppers.problemsassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.crazypeppers.problemsassistant.data.dto.Problem

/**
 * Адаптер для представления списка проблем
 *
 * @constructor Конструктор
 * @param context текущий контекст
 * @param problemList список проблем
 */
class ProblemArrayAdapter(context: Context, problemList: List<Problem>) :
    ArrayAdapter<Problem>(context, android.R.layout.simple_list_item_1, problemList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val problem: Problem? = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, null)
        }
        (view?.findViewById(android.R.id.text1) as TextView).text = problem?.problemName
        return view
    }
}