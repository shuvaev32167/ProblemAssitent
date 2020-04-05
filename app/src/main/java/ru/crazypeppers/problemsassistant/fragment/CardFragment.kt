package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.layout_variants.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener

/**
 * Фрагмент отвечающий за оценивание карты
 */
class CardFragment : Fragment(), OnBackPressedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val application = activity.application as DataApplication

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        var positionProblem = NOT_POSITION
        var positionCard = NOT_POSITION
        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
        }

        seekBarVariants.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val score = progress - 5
                activity.title = when {
                    score > 0 -> getString(R.string.advantageFragmentLabel)
                    score < 0 -> getString(R.string.disadvantageFragmentLabel)
                    else -> getString(R.string.advantage_disadvantageFragmentLabel)
                }
                scoreSeekBar.text = score.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
            val card = application.data[positionProblem][positionCard]
            activity.title = when (card.type) {
                CardType.LINER_ADVANTAGE -> getString(R.string.advantageFragmentLabel)
                CardType.LINER_DISADVANTAGE -> getString(R.string.disadvantageFragmentLabel)
                else -> getString(R.string.advantage_disadvantageFragmentLabel)
            }
            cardName.text = card.name
            if (card.description.isBlank()) {
                cardDescription.visibility = GONE
            } else {
                cardDescription.visibility = VISIBLE
                cardDescription.text = card.description
            }
            if (card.points.isEmpty()) {
                seekBarVariants.progress = card.points.last().score + 5
                confirmButton.visibility = GONE
                makeNewScoreButton.setText(R.string.makeScoreButton)
            } else {
                confirmButton.visibility = VISIBLE
                makeNewScoreButton.setText(R.string.makeNewScoreButton)
            }

            informationText.text = getSssText(card)
        }

        cancelButton.setOnClickListener {
            onBackPressed()
        }

        saveButton.setOnClickListener {
            val score = seekBarVariants.progress - 5
            val card = application.data[positionProblem][positionCard]
            card.add(Point(score))
            application.saveData()
            informationText.text = getSssText(card)
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val card = application.data[positionProblem][positionCard]
            card.add(Point(card.points.first().score))
        }

        makeNewScoreButton.setOnClickListener {
            newLayoutButtons.visibility = GONE
            informationText.visibility = GONE
            frameVariant.visibility = VISIBLE
            layoutButtons.visibility = VISIBLE
        }
    }

    private fun getSssText(card: Card): String {
        return if (card.points.isNotEmpty()) {
            String.format(
                getString(R.string.informationText),
                when (card.type) {
                    CardType.LINER_ADVANTAGE -> getString(R.string.advantage)
                    CardType.LINER_DISADVANTAGE -> getString(R.string.disadvantage)
                    else -> getString(R.string.advantage_disadvantage)
                },
                card.name,
                card.points.first().score,
                DATE_FORMAT.format(card.points.first().cdate.time)
            )
        } else {
            String.format(
                getString(R.string.informationTextNotScore),
                card.name
            )
        }
    }

    override fun onBackPressed(): Boolean {
        return if (newLayoutButtons.visibility != VISIBLE || informationText.visibility != VISIBLE) {
            newLayoutButtons.visibility = VISIBLE
            informationText.visibility = VISIBLE
            frameVariant.visibility = GONE
            layoutButtons.visibility = GONE
            true
        } else {
            false
        }
    }
}
