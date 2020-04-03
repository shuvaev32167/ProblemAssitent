package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_card_edit.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType

/**
 * Фрагмент отвечающий за редактирование существующей карты или добавление новой.
 */
class CardEditFragment : Fragment() {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION
    private var card: Card? = null

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
        val application = activity.application as DataApplication


        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
            if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
                card = application.data[positionProblem][positionCard]
                cardName.setText(card!!.name)
                cardName.setSelection(card?.name?.length ?: 0)
                cardDescription.setText(card?.description)
            } else {
                card = null
            }
        }

        if (card != null) {
            activity.title = when (card!!.type) {
                CardType.LINER_ADVANTAGE -> getString(R.string.advantageEditLabel)
                CardType.LINER_DISADVANTAGE -> getString(R.string.disadvantageEditLabel)
                else -> getString(R.string.advantage_disadvantageEditLabel)
            }
            layoutVariants.visibility = GONE
        } else {
            activity.title = getString(R.string.advantage_disadvantageNewLabel)
            layoutVariants.visibility = VISIBLE
        }

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
            if (card != null) {
                when (card!!.type) {
                    CardType.LINER_ADVANTAGE -> {
                        titleId = R.string.cardAdvantageNameBusyTitle
                        messageId = R.string.cardAdvantageNameBusyMessage
                    }
                    CardType.LINER_DISADVANTAGE -> {
                        titleId = R.string.cardDisadvantageNameBusyTitle
                        messageId = R.string.cardDisadvantageNameBusyMessage
                    }
                    else -> {
                        titleId = R.string.cardAdvantage_DisadvantageNameBusyTitle
                        messageId = R.string.cardAdvantage_DisadvantageNameBusyMessage
                    }
                }
            } else {
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
            if (positionCard != NOT_POSITION) {
                val card = problem[positionCard]
                if (problem.hasCardWithName(newName, card)) {
                    alert.show()
                    return@setOnClickListener
                } else {
                    card.name = newName
                    card.description = cardDescription.text.toString()
                }
            } else {
                if (problem.hasCardWithName(newName)) {
                    alert.show()
                    return@setOnClickListener
                } else {
                    problem.add(
                        Card(
                            cardName = newName,
                            cardDescription = cardDescription.text.toString(),
                            parent = problem,
                            points = mutableListOf(
                                Point(
                                    seekBarVariants.progress - 5
                                )
                            )
                        )
                    )
                }
            }
            application.saveData()
            findNavController().popBackStack()
        }

        seekBarVariants.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val score = progress - 5
                activity.title = when {
                    score < 0 -> getString(R.string.disadvantageNewLabel)
                    score > 0 -> getString(R.string.advantageNewLabel)
                    else -> getString(R.string.advantage_disadvantageNewLabel)
                }
                scoreSeekBar.text = score.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
