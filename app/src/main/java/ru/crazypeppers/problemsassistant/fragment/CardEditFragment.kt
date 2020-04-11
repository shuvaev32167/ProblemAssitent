package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_card_edit.*
import kotlinx.android.synthetic.main.layout_variants.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener

/**
 * Фрагмент отвечающий за редактирование существующей карты или добавление новой.
 */
class CardEditFragment : Fragment(), OnBackPressedListener {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION
    private lateinit var card: Card
    private lateinit var problem: Problem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val application = activity.application as DataApplication


        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
            problem = application.data[positionProblem]
            card = application.data[positionProblem][positionCard]
            cardName.setText(card.name)
            cardName.setSelection(card.name.length)
            cardDescription.setText(card.description)

            if (problem.type == ProblemType.LINE) {
                descartesSquaredLayout.visibility = GONE
            } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                descartesSquaredLayout.visibility = VISIBLE
            }
        }

        setActivityTitle(card)

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        saveButton.setOnClickListener {
            if (positionProblem == NOT_POSITION) {
                findNavController().popBackStack()
                return@setOnClickListener
            }
            val newName = cardName.text.toString()

            val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
            val titleId: Int
            val messageId: Int
            if (problem.type == ProblemType.LINE) {
                when {
                    seekBarVariants.progress > 5 -> {
                        titleId = R.string.cardAdvantageNameBusyTitle
                        messageId = R.string.cardAdvantageNameBusyMessage
                    }
                    seekBarVariants.progress < 5 -> {
                        titleId = R.string.cardDisadvantageNameBusyTitle
                        messageId = R.string.cardDisadvantageNameBusyMessage
                    }
                    else -> {
                        titleId = R.string.cardAdvantage_DisadvantageNameBusyTitle
                        messageId = R.string.cardAdvantage_DisadvantageNameBusyMessage
                    }
                }
            } else {
                titleId = R.string.cardDescartesSquaredNameBusyTitle
                messageId = R.string.cardDescartesSquaredNameBusyMessage
            }
            adb.setTitle(titleId)
            adb.setMessage(
                String.format(
                    getString(messageId),
                    newName
                )
            )
            adb.setIcon(android.R.drawable.ic_dialog_alert)
            adb.setNeutralButton(R.string.fixButton, null)
            val alert = adb.create()
            val problem = application.data[positionProblem]
            if (problem.hasCardWithName(newName, card)) {
                alert.show()
                return@setOnClickListener
            } else {
                card.name = newName
                card.description = cardDescription.text.toString()
                if (problem.type == ProblemType.DESCARTES_SQUARED) {
                    card.type = getCardTypeFromSpinner()
                }
            }
            application.saveData()
            findNavController().popBackStack()
        }
    }

    private fun setActivityTitle(card: Card) {
        activity?.title = when (card.type) {
            CardType.LINEAR_ADVANTAGE -> getString(R.string.advantageEditLabel)
            CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantageEditLabel)
            else -> getString(R.string.advantage_disadvantageEditLabel)
        }
    }

    private fun getCardTypeFromSpinner(): CardType {
        return when (descartesSquaredQuarterSpinner.selectedItemPosition) {
            0 -> CardType.SQUARE_DO_HAPPEN
            1 -> CardType.SQUARE_NOT_DO_HAPPEN
            2 -> CardType.SQUARE_DO_NOT_HAPPEN
            else -> CardType.SQUARE_NOT_DO_NOT_HAPPEN
        }
    }
}
