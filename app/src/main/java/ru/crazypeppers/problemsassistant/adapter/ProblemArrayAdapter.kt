package ru.crazypeppers.problemsassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.extension.toStringRound
import ru.crazypeppers.problemsassistant.util.hyperlinkStyle

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
                .inflate(R.layout.element_problem_list, null)
        }
        if (problem != null) {
            (view?.findViewById(android.R.id.text1) as TextView).text = problem.name
            val problemPointLabel = view.findViewById(R.id.problemPoint) as TextView
            val adb: AlertDialog.Builder = AlertDialog.Builder(context)
            adb.setTitle(R.string.informationTitle)
            adb.setIcon(android.R.drawable.ic_dialog_info)
            adb.setNeutralButton(R.string.okButton, null)
            if (problem.type == ProblemType.DESCARTES_SQUARED) {
                adb.setMessage(
                    String.format(
                        context.getString(R.string.informationProblemDescartesSquaredAlertBody),
                        problem.name
                    )
                )
                problemPointLabel.text = context.getString(R.string.descartesSquaredShort)
                problemPointLabel.hyperlinkStyle()
                problemPointLabel.setOnClickListener {
                    adb.create().show()
                }
            } else {
                val avgPoint = problem.calculateScoreProblem()
                val avpPointString = if (avgPoint.isNaN()) "0.00" else avgPoint.toStringRound(2)
                problemPointLabel.text = avpPointString

                adb.setMessage(
                    String.format(
                        context.getString(R.string.informationProblemAlertBody),
                        avpPointString
                    )
                )

                problemPointLabel.setOnClickListener {
                    adb.create().show()
                }
                problemPointLabel.hyperlinkStyle()
            }
        }
        return view!!
    }
}
