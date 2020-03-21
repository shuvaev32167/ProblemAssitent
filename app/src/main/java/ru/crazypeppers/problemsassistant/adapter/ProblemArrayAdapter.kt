package ru.crazypeppers.problemsassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.hyperlinkStyle
import ru.crazypeppers.problemsassistant.roundTo

/**
 * Адаптер для представления списка проблем
 *
 * @constructor Конструктор
 * @param context текущий контекст
 * @param problemList список проблем
 */
class ProblemArrayAdapter(context: Context, problemList: List<Problem>) :
    ArrayAdapter<Problem>(context, R.layout.element_problem_list, problemList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val problem: Problem? = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.element_problem_list, RelativeLayout(context))
        }
        if (problem != null) {
            (view?.findViewById(android.R.id.text1) as TextView).text = problem.name
            val avgPointsList = problem.cards.map { it.avgPoints }
            val avgPoint = avgPointsList.sum() / avgPointsList.size
            val avpPointString = if (avgPoint.isNaN()) "0.00" else avgPoint.roundTo(2).toString()
            val problemPointLabel = view.findViewById(R.id.problemPoint) as TextView
            problemPointLabel.text = avpPointString
            problemPointLabel.setOnClickListener {
                val adb: AlertDialog.Builder = AlertDialog.Builder(context)
                adb.setTitle(R.string.informationTitle)
                adb.setMessage(
                    String.format(
                        context.getString(R.string.informationProblemAllertBody),
                        avpPointString
                    )
                )
                adb.setIcon(android.R.drawable.ic_dialog_info)
                adb.setNeutralButton(R.string.okButton, null)
                adb.create().show()
            }
            problemPointLabel.hyperlinkStyle()

        }
        return view!!
    }
}