package ru.crazypeppers.problemsassistant.fragment.tabled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_analyze_problem_summery.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.toStringRound

/**
 * Фрагмент отвечающий за общую инфвормацию в анализе проблемы
 */
class
AnalyzeProblemSummaryFragment : Fragment() {

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
            val avgPoint = problem.calculateScoreProblem()

            summeryInformation.text = String.format(
                getString(R.string.avgPointsLabel), problem.name,
                if (avgPoint.isNaN()) "0.00" else avgPoint.toStringRound(2)
            )

            countMotivation.text = String.format(
                getString(R.string.countMotivationLabel),
                problem.cards.filter { it.type == CardType.LINEAR_ADVANTAGE }.count()
            )

            countAnchor.text = String.format(
                getString(R.string.countAnchorLabel),
                problem.cards.filter { it.type == CardType.LINEAR_DISADVANTAGE }.count()
            )

            summeryCountCard.text = String.format(
                getString(R.string.summaryCountCardLabel), problem.cards.count()
            )

            val assessments = problem.cards.filterIsInstance<LinearCard>().flatMap { it.points }
                .sortedBy { it.cdate }

            if (assessments.isNotEmpty()) {
                dateOfFirstProblemAssessment.text = String.format(
                    getString(R.string.dateOfFirstProblemAssessmentLabel),
                    DATE_FORMAT.format(assessments.first().cdate.time)
                )

                dateOfLastProblemAssessment.text = String.format(
                    getString(R.string.dateOfLastProblemAssessmentLabel),
                    DATE_FORMAT.format(assessments.last().cdate.time)
                )
            } else {
                dateOfFirstProblemAssessment.visibility = GONE
                dateOfLastProblemAssessment.visibility = GONE
            }
        }
    }
}
