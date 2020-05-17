package ru.crazypeppers.problemsassistant.fragment.tabled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_analyze_problem_summery.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.util.fromHtml
import ru.crazypeppers.problemsassistant.util.toStringRound

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
            if (problem.type == ProblemType.LINE) {
                linearProblemLayout.visibility = VISIBLE
                descartesSquaredProblemLayout.visibility = GONE

                val cards = problem.cards.filterIsInstance<LinearCard>()

                val avgPoint = problem.calculateScoreProblem()

                summeryInformation.text = String.format(
                    getString(R.string.avgPointsLabel), problem.name,
                    if (avgPoint.isNaN()) "0.00" else avgPoint.toStringRound(2)
                )

                countMotivation.text = String.format(
                    getString(R.string.countMotivationLabel),
                    cards.filter { it.type == CardType.LINEAR_ADVANTAGE }.count()
                )

                countAnchor.text = String.format(
                    getString(R.string.countAnchorLabel),
                    cards.filter { it.type == CardType.LINEAR_DISADVANTAGE }.count()
                )

                summeryCountCard.text = String.format(
                    getString(R.string.summaryCountCardLabel), cards.count()
                )

                val assessments = cards.flatMap { it.points }
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
            } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                linearProblemLayout.visibility = GONE
                descartesSquaredProblemLayout.visibility = VISIBLE

                val cards = problem.cards.filterIsInstance<DescartesSquaredCard>()
                    .sortedBy { it.createCardDate }

                descartesSquaredCountIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_DO_HAPPEN }
                        )
                    )

                descartesSquaredCountIIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_NOT_DO_HAPPEN }
                        )
                    )

                descartesSquaredCountIIIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIIIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_DO_NOT_HAPPEN }
                        )
                    )

                descartesSquaredCountIVGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIVQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_NOT_DO_NOT_HAPPEN }
                        )
                    )

                descartesSquaredSummeryCountCard.text =
                    getString(R.string.summaryCountCardLabel, cards.count())

                if (cards.isNotEmpty()) {
                    descartesSquaredDateOfFistCardCreate.visibility = VISIBLE
                    descartesSquaredDateOfLastCardCreate.visibility = VISIBLE

                    descartesSquaredDateOfFistCardCreate.text = getString(
                        R.string.descartesSquaredDateOfFistCardCreateLabel,
                        DATE_FORMAT.format(cards.first().createCardDate.time)
                    )

                    descartesSquaredDateOfLastCardCreate.text = getString(
                        R.string.descartesSquaredDateOfLastCardCreateLabel,
                        DATE_FORMAT.format(cards.last().createCardDate.time)
                    )
                } else {
                    descartesSquaredDateOfFistCardCreate.visibility = GONE
                    descartesSquaredDateOfLastCardCreate.visibility = GONE
                }
            }
        }
    }
}
