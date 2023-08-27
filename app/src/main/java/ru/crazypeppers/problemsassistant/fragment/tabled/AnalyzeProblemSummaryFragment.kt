package ru.crazypeppers.problemsassistant.fragment.tabled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import ru.crazypeppers.problemsassistant.databinding.FragmentAnalyzeProblemSummeryBinding
import ru.crazypeppers.problemsassistant.extension.toStringRound
import ru.crazypeppers.problemsassistant.util.fromHtml

/**
 * Фрагмент отвечающий за общую инфвормацию в анализе проблемы
 */
class AnalyzeProblemSummaryFragment : Fragment() {

    private var _binding: FragmentAnalyzeProblemSummeryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeProblemSummeryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        val arg = arguments
        if (arg != null) {
            val positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            val problem = (activity.application as DataApplication).data[positionProblem]
            if (problem.type == ProblemType.LINE) {
                binding.linearProblemLayout.visibility = VISIBLE
                binding.descartesSquaredProblemLayout.visibility = GONE

                val cards = problem.cards.filterIsInstance<LinearCard>()

                val avgPoint = problem.calculateScoreProblem()

                binding.summeryInformation.text = String.format(
                    getString(R.string.avgPointsLabel), problem.name,
                    if (avgPoint.isNaN()) "0.00" else avgPoint.toStringRound(2)
                )

                binding.countMotivation.text = String.format(
                    getString(R.string.countMotivationLabel),
                    cards.filter { it.type == CardType.LINEAR_ADVANTAGE }.count()
                )

                binding.countAnchor.text = String.format(
                    getString(R.string.countAnchorLabel),
                    cards.filter { it.type == CardType.LINEAR_DISADVANTAGE }.count()
                )

                binding.summeryCountCard.text = String.format(
                    getString(R.string.summaryCountCardLabel), cards.count()
                )

                val assessments = cards.flatMap { it.points }
                    .sortedBy { it.cdate }

                if (assessments.isNotEmpty()) {
                    binding.dateOfFirstProblemAssessment.text = String.format(
                        getString(R.string.dateOfFirstProblemAssessmentLabel),
                        DATE_FORMAT.format(assessments.first().cdate.time)
                    )

                    binding.dateOfLastProblemAssessment.text = String.format(
                        getString(R.string.dateOfLastProblemAssessmentLabel),
                        DATE_FORMAT.format(assessments.last().cdate.time)
                    )
                } else {
                    binding.dateOfFirstProblemAssessment.visibility = GONE
                    binding.dateOfLastProblemAssessment.visibility = GONE
                }
            } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                binding.linearProblemLayout.visibility = GONE
                binding.descartesSquaredProblemLayout.visibility = VISIBLE

                val cards = problem.cards.filterIsInstance<DescartesSquaredCard>()
                    .sortedBy { it.createCardDate }

                binding.descartesSquaredCountIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_DO_HAPPEN }
                        )
                    )

                binding.descartesSquaredCountIIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_NOT_DO_HAPPEN }
                        )
                    )

                binding.descartesSquaredCountIIIGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIIIQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_DO_NOT_HAPPEN }
                        )
                    )

                binding.descartesSquaredCountIVGroup.text =
                    fromHtml(
                        getString(R.string.descartesSquaredGroupCardCountLabel,
                            getString(R.string.descartesSquaredIVQuarterFotGetString),
                            cards.count { it.type == CardType.SQUARE_NOT_DO_NOT_HAPPEN }
                        )
                    )

                binding.descartesSquaredSummeryCountCard.text =
                    getString(R.string.summaryCountCardLabel, cards.count())

                if (cards.isNotEmpty()) {
                    binding.descartesSquaredDateOfFistCardCreate.visibility = VISIBLE
                    binding.descartesSquaredDateOfLastCardCreate.visibility = VISIBLE

                    binding.descartesSquaredDateOfFistCardCreate.text = getString(
                        R.string.descartesSquaredDateOfFistCardCreateLabel,
                        DATE_FORMAT.format(cards.first().createCardDate.time)
                    )

                    binding.descartesSquaredDateOfLastCardCreate.text = getString(
                        R.string.descartesSquaredDateOfLastCardCreateLabel,
                        DATE_FORMAT.format(cards.last().createCardDate.time)
                    )
                } else {
                    binding.descartesSquaredDateOfFistCardCreate.visibility = GONE
                    binding.descartesSquaredDateOfLastCardCreate.visibility = GONE
                }
            }
        }
    }
}
