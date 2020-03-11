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
import ru.crazypeppers.problemsassistant.data.dto.Card

/**
 * Фрагмент, отвечающий за список мотиваций и якорей в анализе проблемы
 */
class AnalyzeProblemMotivationList : ListFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity

        val arg = arguments
        if (arg != null) {
            val positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            val lambda = arg.getSerializable(LAMBDA_TEXT) as (Card) -> Boolean
            val problem = (activity.application as DataApplication).data[positionProblem]
            val motivationsList = problem.cards.filter { lambda(it) }
                .sortedByDescending { it.calculateAvgPoints() }

            listAdapter = CardArrayAdapter(
                activity,
                motivationsList
            ) { "${it.cardName} (${it.calculateAvgPoints()})" }
        }
    }
}