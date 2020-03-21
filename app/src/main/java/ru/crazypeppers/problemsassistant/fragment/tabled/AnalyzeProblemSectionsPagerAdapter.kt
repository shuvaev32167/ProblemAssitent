package ru.crazypeppers.problemsassistant.fragment.tabled

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.LAMBDA_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
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
    private val arguments: Bundle?
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val PAGE_COUNT = 3
    private val ANALYZE_PROBLEM_TABS = context.resources.getTextArray(R.array.analyzeProblemTabs)

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val analyzeProblemSummeryFragment = AnalyzeProblemSummeryFragment()
                analyzeProblemSummeryFragment.arguments = arguments
                return analyzeProblemSummeryFragment
            }
            1, 2 -> {
                val analyzeProblemMotivationList = AnalyzeProblemCardList()
                val bundle = Bundle(arguments)
                bundle.putSerializable(
                    LAMBDA_TEXT,
                    if (position == 1) { card: Card ->
                        card.type == CardType.LINER_MOTIVATIONS
                    }
                    else { card: Card ->
                        card.type == CardType.LINER_ANCHOR
                    } as Serializable
                )
                analyzeProblemMotivationList.arguments = bundle
                return analyzeProblemMotivationList
            }
            else -> PageFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ANALYZE_PROBLEM_TABS[position]
    }

}