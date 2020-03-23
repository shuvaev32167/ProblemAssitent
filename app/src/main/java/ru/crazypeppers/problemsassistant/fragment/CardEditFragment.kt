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

/**
 * Фрагмент отвечающий за редактирование существующей карты или добавление новой.
 */
class CardEditFragment : Fragment() {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION

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
                val card = application.data[positionProblem][positionCard]
                cardName.setText(card.name)
                cardName.setSelection(card.name.length)
                cardDescription.setText(card.description)
            }
        }

        if (positionCard != NOT_POSITION) {
            activity.title = getString(R.string.card_edit_fragment_label)
            layoutVariants.visibility = GONE
        } else {
            activity.title = getString(R.string.card_new_fragment_label)
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
            adb.setTitle(R.string.cardNameBusyTitle)
            adb.setMessage(
                String.format(
                    getString(R.string.cardNameBusyMessage),
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
                scoreSeekBar.text = (progress - 5).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
