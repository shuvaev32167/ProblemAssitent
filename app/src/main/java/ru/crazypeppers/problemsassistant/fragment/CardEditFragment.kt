package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_card_edit.*
import kotlinx.android.synthetic.main.fragment_card_edit.cardName
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Point
import java.util.*

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
                cardName.setText(card.cardName)
                cardName.setSelection(card.cardName.length)
                cardDescription.setText(card.cardDescription)
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
            if (positionCard != NOT_POSITION) {
                val card = application.data[positionProblem][positionCard]
                card.cardName = cardName.text.toString()
                card.cardDescription = cardDescription.text.toString()
            } else {
                application.data[positionProblem].add(
                    Card(
                        cardName = cardName.text.toString(),
                        cardDescription = cardDescription.text.toString(),
                        points = mutableListOf(
                            Point(seekBarVariants.progress - 5, Date())
                        )
                    )
                )
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
