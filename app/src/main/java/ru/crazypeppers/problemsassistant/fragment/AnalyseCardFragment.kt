package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_analyse_card.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.roundTo
import java.text.DateFormat
import java.text.DateFormat.SHORT

/**
 * A simple [Fragment] subclass.
 */
class AnalyseCardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analyse_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        val application = activity.application as DataApplication

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()
        activity.title = getString(R.string.analyzeCardFragmentLabel)

        var positionProblem = NOT_POSITION
        var positionCard = NOT_POSITION
        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
        }

        if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
            val card = application.data[positionProblem][positionCard]
            val lineSet = linkedMapOf<String, Float>()
            val dateFormat = DateFormat.getDateInstance(SHORT)
            card.points.forEach { lineSet[dateFormat.format(it.date)] = it.score.toFloat() }
            lineChart.animate(lineSet)
            lineChart.labelsFormatter = { it.roundTo(2).toString() }

            graphLabel.text =
                String.format(getString(R.string.analyzeCardFragmentLabel), card.cardName)
        }

    }
}
