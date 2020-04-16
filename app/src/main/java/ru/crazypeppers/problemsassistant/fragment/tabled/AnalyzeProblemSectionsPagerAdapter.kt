package ru.crazypeppers.problemsassistant.fragment.tabled

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.LAMBDA_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import java.io.Serializable

/**
 * Контролер фрагментов во вкладках анализа проблемы
 *
 * @property arguments список аргументов для передачи во фрагменты
 *
 * @constructor
 * @param fm
 * @param context
 */
class AnalyzeProblemSectionsPagerAdapter(
    fm: FragmentManager,
    context: Context,
    private val arguments: Bundle?,
    private val problem: Problem
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    /**
     * Число вкладок при анализе линейной проблемы
     */
    private val analyzeLinearProblemTabCount = 3

    /**
     * Загаловки вкладок для анализа линейной проблемы
     */
    private val analyzeLinearProblemTabNames =
        context.resources.getTextArray(R.array.analyzeLinearProblemTabs)

    /**
     * Число вкладок при анализе проблемы решаемой по квадрату декартаDESCARTES_SQUARED
     */
    private val analyzeDescartesSquaredProblemTabCount = 5

    /**
     * Заголовки вкладок для анализа проблемы, решаемый по квадрату декарта
     */
    private val analyzeDescartesSquaredProblemTabNames =
        context.resources.getTextArray(R.array.analyzeDescartesSquaredProblemTabs)

    override fun getCount(): Int {
        return if (problem.type == ProblemType.LINE) {
            analyzeLinearProblemTabCount
        } else {
            analyzeDescartesSquaredProblemTabCount
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val analyzeProblemSummeryFragment = AnalyzeProblemSummaryFragment()
                analyzeProblemSummeryFragment.arguments = arguments
                return analyzeProblemSummeryFragment
            }
            1, 2, 3, 4 -> {
                val analyzeProblemMotivationList = AnalyzeProblemCardList()
                val bundle = Bundle(arguments)
                bundle.putSerializable(
                    LAMBDA_TEXT,
                    when (position) {
                        1 -> { card: BaseCard ->
                            card.type == CardType.LINEAR_ADVANTAGE || card.type == CardType.SQUARE_DO_HAPPEN
                        }
                        2 -> { card: BaseCard ->
                            card.type == CardType.LINEAR_DISADVANTAGE || card.type == CardType.SQUARE_NOT_DO_HAPPEN
                        }
                        3 -> { card: BaseCard ->
                            card.type == CardType.SQUARE_DO_NOT_HAPPEN
                        }
                        else -> { card: BaseCard ->
                            card.type == CardType.SQUARE_NOT_DO_NOT_HAPPEN
                        }
                    } as Serializable
                )
                analyzeProblemMotivationList.arguments = bundle
                return analyzeProblemMotivationList
            }
            else -> PageFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (problem.type == ProblemType.LINE) {
            analyzeLinearProblemTabNames[position]
        } else {
            analyzeDescartesSquaredProblemTabNames[position]
        }
    }

}