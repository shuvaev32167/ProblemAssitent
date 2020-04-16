package ru.crazypeppers.problemsassistant.fragment.tabled

import android.os.Bundle
import android.view.View
import androidx.fragment.app.ListFragment
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.adapter.CardArrayAdapter
import ru.crazypeppers.problemsassistant.data.LAMBDA_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard

/**
 * Фрагмент, отвечающий за список карт в анализе проблемы
 */
class AnalyzeProblemCardList : ListFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity

        val arg = arguments
        if (arg != null) {
            val positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            val lambda = arg.getSerializable(LAMBDA_TEXT) as (BaseCard) -> Boolean
            val problem = (activity.application as DataApplication).data[positionProblem]
            val cardList = problem.cards.filter { lambda(it) }
                .sortedByDescending { if (it is LinearCard) it.avgPoints else 0f }

            listAdapter = CardArrayAdapter(activity, cardList)
        }
    }
}