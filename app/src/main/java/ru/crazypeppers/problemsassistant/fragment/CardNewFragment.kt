package ru.crazypeppers.problemsassistant.fragment

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_card_new.*
import kotlinx.android.synthetic.main.fragment_card_new.cardDescription
import kotlinx.android.synthetic.main.fragment_card_new.cardName
import kotlinx.android.synthetic.main.layout_variants.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener

/**
 * A simple [Fragment] subclass.
 */
class CardNewFragment : Fragment(), OnBackPressedListener {
    private var positionProblem = NOT_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_new, container, false)
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
        }

        val problem = application.data[positionProblem]

        if (problem.type == ProblemType.LINE) {
            activity.title = getString(R.string.advantage_disadvantageNewLabel)
            layoutVariants.visibility = VISIBLE
            checkScoreNow.visibility = VISIBLE
        } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
            activity.title = getString(R.string.cardNewLabel)
            layoutVariants.visibility = GONE
            checkScoreNow.visibility = GONE
            descartesSquaredLayout.visibility = VISIBLE
        }


        checkScoreNow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layoutVariants.visibility = GONE
                activity.title = getString(R.string.advantage_disadvantageNewLabel)
            } else {
                layoutVariants.visibility = VISIBLE
                setActivityTitle(seekBarVariants.progress - 5)
            }
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

            if (problem.hasCardWithName(newName)) {
                alert.show()
                return@setOnClickListener
            } else {
                if (problem.type == ProblemType.LINE) {
                    if (checkScoreNow.isChecked) {
                        problem.add(LinearCard(newName, cardDescription.text.toString()))
                    } else {
                        problem.add(
                            LinearCard(
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
                } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                    problem.add(
                        DescartesSquaredCard(
                            cardName = newName,
                            cardDescription = cardDescription.text.toString(),
                            cardType = getCardTypeFromSpinner()
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
                setActivityTitle(score)
                scoreSeekBar.text = score.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    /**
     * Установка заголовка активити по оценке карты
     *
     * @param score оценка карты
     */
    private fun setActivityTitle(score: Int) {
        activity?.title = when {
            score < 0 -> getString(R.string.disadvantageNewLabel)
            score > 0 -> getString(R.string.advantageNewLabel)
            else -> getString(R.string.advantage_disadvantageNewLabel)
        }
    }

    /**
     * Получения типа карты, согласно выпадающему меню [descartesSquaredQuarterSpinner]
     *
     * @return Тип карты
     */
    private fun getCardTypeFromSpinner(): CardType {
        return when (descartesSquaredQuarterSpinner.selectedItemPosition) {
            0 -> CardType.SQUARE_DO_HAPPEN
            1 -> CardType.SQUARE_NOT_DO_HAPPEN
            2 -> CardType.SQUARE_DO_NOT_HAPPEN
            else -> CardType.SQUARE_NOT_DO_NOT_HAPPEN
        }
    }

}
