package ru.crazypeppers.problemsassistant.fragment.tabled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_analyze_problem_summery.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.roundTo

/**
 * Фрагмент отвечающий за общую инфвормацию в анализе проблемы
 */
class AnalyzeProblemSummeryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analyze_problem_summery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        val arg = arguments
        if (arg != null) {
            val positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            val problem = (activity.application as DataApplication).data[positionProblem]
            val avgPointsList = problem.cards.map { it.calculateAvgPoints() }
            val avgPoint = avgPointsList.sum() / avgPointsList.size

            summeryInformation.text = String.format(
                getString(R.string.avgPointsLabel), problem.problemName,
                if (avgPoint.isNaN()) "0.00" else avgPoint.roundTo(2)
            )
        }
    }
}
