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
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.fromHtml
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener

/**
 * Фрагмент отвечающий за оценивание карты
 */
class CardFragment : Fragment(), OnBackPressedListener {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION

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
                CardType.LINEAR_ADVANTAGE -> getString(R.string.advantageFragmentLabel)
                CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantageFragmentLabel)
                CardType.NONE -> getString(R.string.advantage_disadvantageFragmentLabel)
                else -> getString(R.string.cardLabel)
            }
            cardName.text = card.name
            cardNameTitle.text = String.format(
                getString(R.string.cardNameTitle),
                getString(
                    when (card.type) {
                        CardType.LINEAR_ADVANTAGE -> R.string.advantageR
                        CardType.LINEAR_DISADVANTAGE -> R.string.disadvantageR
                        else -> R.string.advantage_disadvantageR
                    }
                )
            )
            if (card.description.isBlank()) {
                cardDescriptionLayout.visibility = GONE
            } else {
                cardDescriptionLayout.visibility = VISIBLE
                cardDescription.text = card.description
                cardDescriptionTitle.text = String.format(
                    getString(R.string.cardDescriptionTitle),
                    getString(
                        when (card.type) {
                            CardType.LINEAR_ADVANTAGE -> R.string.advantageR
                            CardType.LINEAR_DISADVANTAGE -> R.string.disadvantageR
                            else -> R.string.advantage_disadvantageR
                        }
                    )
                )
            }
            if (card is LinearCard) {
                processingAppearanceOfElementsDependingOnAvailabilityOfPoints(
                    card.points
                )
            } else if (card is DescartesSquaredCard) {
                newLayoutButtons.visibility = GONE
            }

            informationText.text = getInformationTextText(card)
        }

        cancelButton.setOnClickListener {
            onBackPressed()
        }

        saveButton.setOnClickListener {
            val score = seekBarVariants.progress - 5
            val card = application.data[positionProblem][positionCard] as LinearCard
            card.add(Point(score))
            application.saveData()
            informationText.text = getInformationTextText(card)
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val card = application.data[positionProblem][positionCard] as LinearCard
            card.add(Point(card.points.first().score))
            application.saveData()
            informationText.text = getInformationTextText(card)
        }

        makeNewScoreButton.setOnClickListener {
            newLayoutButtons.visibility = GONE
            informationText.visibility = GONE
            frameVariant.visibility = VISIBLE
            layoutButtons.visibility = VISIBLE
            val card = application.data[positionProblem][positionCard]
            if (card is LinearCard)
                seekBarVariants.progress = (card.points.maxBy { it.cdate }?.score ?: 0) + 5
        }
    }

    /**
     * Определение информационного текста, на основе данных карты [card].
     * В зависимости от типа проблемы ([BaseCard.parent]), будет вернут или `String` или `Span`.
     *
     * @param card карта
     * @return текст для информационного сообщения
     */
    private fun getInformationTextText(card: BaseCard): CharSequence {
        return if (card is LinearCard)
            if (card.points.isNotEmpty()) {
                String.format(
                    getString(R.string.informationText),
                    when (card.type) {
                        CardType.LINEAR_ADVANTAGE -> getString(R.string.advantage)
                        CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantage)
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
            } else {
            fromHtml(
                getString(
                    R.string.informationDescartesSquaredText,
                    getString(
                        when (card.type) {
                            CardType.SQUARE_DO_HAPPEN -> R.string.descartesSquaredIQuarterFotGetString
                            CardType.SQUARE_NOT_DO_HAPPEN -> R.string.descartesSquaredIIQuarterFotGetString
                            CardType.SQUARE_DO_NOT_HAPPEN -> R.string.descartesSquaredIIIQuarterFotGetString
                            else -> R.string.descartesSquaredIVQuarterFotGetString
                        }
                    )
                )
            )
        }
    }

    override fun onBackPressed(): Boolean {
        return if (newLayoutButtons.visibility != VISIBLE && informationText.visibility != VISIBLE) {
            newLayoutButtons.visibility = VISIBLE
            informationText.visibility = VISIBLE
            frameVariant.visibility = GONE
            layoutButtons.visibility = GONE
            if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
                val card =
                    (requireActivity().application as DataApplication).data[positionProblem][positionCard]
                if (card is LinearCard)
                    processingAppearanceOfElementsDependingOnAvailabilityOfPoints(
                        card.points
                    )
            }
            true
        } else {
            false
        }
    }

    /**
     * Обработка внешнего вида элементов, в зависимости от наличия оценок
     *
     * @param points оценки карты
     */
    private fun processingAppearanceOfElementsDependingOnAvailabilityOfPoints(points: List<Point>) {
        if (points.isEmpty()) {
            seekBarVariants.progress = 5
            confirmButton.visibility = GONE
            makeNewScoreButton.setText(R.string.makeScoreButton)
        } else {
            confirmButton.visibility = VISIBLE
            makeNewScoreButton.setText(R.string.makeNewScoreButton)
        }
    }
}
